package com.example.norsos.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.norsos.data.PreferencesManager
import com.example.norsos.model.AppSettings
import com.example.norsos.model.EmergencyContact
import com.example.norsos.services.EmergencyDispatcher
import com.example.norsos.services.FlashlightController
import com.example.norsos.services.LocationData
import com.example.norsos.services.LocationTracker
import com.example.norsos.services.SirenAudioSynthesizer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NavTab {
    SOS,
    CONTACTS,
    TOOLS,
    SETTINGS,
    DATA_SAFETY
}

class EmergencyViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val siren = SirenAudioSynthesizer()
    private val flashlight = FlashlightController(application)
    val locationTracker = LocationTracker(application)

    private val _activeTab = MutableStateFlow(NavTab.SOS)
    val activeTab: StateFlow<NavTab> = _activeTab.asStateFlow()

    private val _settings = MutableStateFlow(prefs.loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _contacts = MutableStateFlow(prefs.loadContacts())
    val contacts: StateFlow<List<EmergencyContact>> = _contacts.asStateFlow()

    private val _isCountdownActive = MutableStateFlow(false)
    val isCountdownActive: StateFlow<Boolean> = _isCountdownActive.asStateFlow()

    private val _countdownRemaining = MutableStateFlow(0)
    val countdownRemaining: StateFlow<Int> = _countdownRemaining.asStateFlow()

    private val _isEmergencyActive = MutableStateFlow(false)
    val isEmergencyActive: StateFlow<Boolean> = _isEmergencyActive.asStateFlow()

    private val _isSirenPlaying = MutableStateFlow(false)
    val isSirenPlaying: StateFlow<Boolean> = _isSirenPlaying.asStateFlow()

    private val _isWhistlePlaying = MutableStateFlow(false)
    val isWhistlePlaying: StateFlow<Boolean> = _isWhistlePlaying.asStateFlow()

    private val _isFlashlightStrobing = MutableStateFlow(false)
    val isFlashlightStrobing: StateFlow<Boolean> = _isFlashlightStrobing.asStateFlow()

    val currentLocation: StateFlow<LocationData?> = locationTracker.currentLocation
    val isGpsEnabled: StateFlow<Boolean> = locationTracker.isGpsEnabled

    private var countdownJob: Job? = null

    init {
        locationTracker.startUpdates()
    }

    fun setTab(tab: NavTab) {
        _activeTab.value = tab
    }

    fun onSosButtonPressed(context: Context) {
        if (_isEmergencyActive.value || _isCountdownActive.value) {
            stopAllEmergencies()
            return
        }

        val seconds = _settings.value.countdownSeconds
        if (seconds <= 0) {
            executeEmergencyTrigger(context)
        } else {
            startCountdown(context, seconds)
        }
    }

    private fun startCountdown(context: Context, seconds: Int) {
        _isCountdownActive.value = true
        _countdownRemaining.value = seconds
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in seconds downTo 1) {
                _countdownRemaining.value = i
                EmergencyDispatcher.triggerSosVibration(context)
                delay(1000L)
            }
            _isCountdownActive.value = false
            executeEmergencyTrigger(context)
        }
    }

    fun cancelCountdown() {
        countdownJob?.cancel()
        countdownJob = null
        _isCountdownActive.value = false
        _countdownRemaining.value = 0
    }

    private fun executeEmergencyTrigger(context: Context) {
        _isEmergencyActive.value = true
        val conf = _settings.value

        // Haptic feedback
        if (conf.vibrationEnabled) {
            EmergencyDispatcher.triggerSosVibration(context)
        }

        // Siren
        if (conf.sirenEnabled) {
            siren.startSiren()
            _isSirenPlaying.value = true
        }

        // Flashlight strobe
        if (conf.flashlightEnabled) {
            flashlight.startStrobe(100L)
            _isFlashlightStrobing.value = true
        }

        // Location refresh
        locationTracker.readLastKnownLocation()

        // Prepare emergency message
        val loc = currentLocation.value
        val locString = if (loc != null) {
            "${loc.googleMapsUrl} (${loc.formattedCoordinates})"
        } else {
            "Konum alınıyor (GPS bekleniyor)"
        }
        val finalMsg = conf.customMessage.replace("{location}", locString)

        if (!conf.testMode) {
            // Find primary contact or emergency number
            val primary = _contacts.value.firstOrNull { it.isPrimary } ?: _contacts.value.firstOrNull()
            val phone = primary?.phoneNumber ?: conf.emergencyDialNumber

            // Open SMS to all contacts or primary
            EmergencyDispatcher.sendSms(context, phone, finalMsg)

            // Auto-call primary contact if enabled
            if (conf.autoCallPrimary) {
                viewModelScope.launch {
                    delay(1500L) // Allow SMS screen to appear first or user to select
                    EmergencyDispatcher.dialNumber(context, phone)
                }
            }
        }
    }

    fun stopAllEmergencies() {
        cancelCountdown()
        _isEmergencyActive.value = false
        siren.stop()
        _isSirenPlaying.value = false
        _isWhistlePlaying.value = false
        flashlight.turnOffAll()
        _isFlashlightStrobing.value = false
    }

    fun toggleSiren() {
        if (_isSirenPlaying.value) {
            siren.stop()
            _isSirenPlaying.value = false
        } else {
            _isWhistlePlaying.value = false
            siren.startSiren()
            _isSirenPlaying.value = true
        }
    }

    fun toggleWhistle() {
        if (_isWhistlePlaying.value) {
            siren.stop()
            _isWhistlePlaying.value = false
        } else {
            _isSirenPlaying.value = false
            siren.startWhistle()
            _isWhistlePlaying.value = true
        }
    }

    fun toggleFlashlightStrobe() {
        if (_isFlashlightStrobing.value) {
            flashlight.stopStrobe()
            _isFlashlightStrobing.value = false
        } else {
            flashlight.startStrobe(120L)
            _isFlashlightStrobing.value = true
        }
    }

    fun addContact(name: String, phone: String, relationship: String, isPrimary: Boolean) {
        val current = _contacts.value.toMutableList()
        if (isPrimary || current.isEmpty()) {
            for (i in current.indices) {
                current[i] = current[i].copy(isPrimary = false)
            }
        }
        current.add(
            EmergencyContact(
                name = name.trim(),
                phoneNumber = phone.trim(),
                relationship = relationship.trim().ifEmpty { "Yakın" },
                isPrimary = isPrimary || current.isEmpty()
            )
        )
        _contacts.value = current
        prefs.saveContacts(current)
    }

    fun updateContact(updated: EmergencyContact) {
        val current = _contacts.value.toMutableList()
        val index = current.indexOfFirst { it.id == updated.id }
        if (index >= 0) {
            if (updated.isPrimary) {
                for (i in current.indices) {
                    current[i] = current[i].copy(isPrimary = false)
                }
            }
            current[index] = updated
            _contacts.value = current
            prefs.saveContacts(current)
        }
    }

    fun deleteContact(id: String) {
        val current = _contacts.value.filter { it.id != id }.toMutableList()
        if (current.none { it.isPrimary } && current.isNotEmpty()) {
            current[0] = current[0].copy(isPrimary = true)
        }
        _contacts.value = current
        prefs.saveContacts(current)
    }

    fun setPrimaryContact(id: String) {
        val current = _contacts.value.map {
            it.copy(isPrimary = (it.id == id))
        }
        _contacts.value = current
        prefs.saveContacts(current)
    }

    fun setLanguage(lang: com.example.norsos.model.AppLanguage) {
        val oldSettings = _settings.value
        val newCustomMessage = if (oldSettings.customMessage == com.example.norsos.localization.AppStrings.defaultSosMessage(oldSettings.language)) {
            com.example.norsos.localization.AppStrings.defaultSosMessage(lang)
        } else {
            oldSettings.customMessage
        }
        val updated = oldSettings.copy(language = lang, customMessage = newCustomMessage)
        updateSettings(updated)
    }

    fun updateSettings(newSettings: AppSettings) {
        _settings.value = newSettings
        prefs.saveSettings(newSettings)
    }

    fun clearAllData() {
        stopAllEmergencies()
        prefs.clearAllData()
        _settings.value = prefs.loadSettings()
        _contacts.value = prefs.loadContacts()
    }

    override fun onCleared() {
        super.onCleared()
        stopAllEmergencies()
        locationTracker.stopUpdates()
    }
}
