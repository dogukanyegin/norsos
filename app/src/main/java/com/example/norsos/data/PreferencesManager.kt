package com.example.norsos.data

import android.content.Context
import android.content.SharedPreferences
import com.example.norsos.model.AppSettings
import com.example.norsos.model.EmergencyContact
import org.json.JSONArray
import org.json.JSONObject

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("norsos_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CONTACTS = "key_emergency_contacts"
        private const val KEY_CUSTOM_MSG = "key_custom_msg"
        private const val KEY_COUNTDOWN = "key_countdown"
        private const val KEY_SIREN = "key_siren_enabled"
        private const val KEY_FLASHLIGHT = "key_flashlight_enabled"
        private const val KEY_VIBRATION = "key_vibration_enabled"
        private const val KEY_AUTO_CALL = "key_auto_call"
        private const val KEY_EMERGENCY_NUMBER = "key_emergency_number"
        private const val KEY_TEST_MODE = "key_test_mode"
        private const val KEY_LANGUAGE = "key_language"
    }

    fun loadSettings(): AppSettings {
        val langCode = prefs.getString(KEY_LANGUAGE, "tr") ?: "tr"
        val lang = com.example.norsos.model.AppLanguage.entries.find { it.code == langCode } ?: com.example.norsos.model.AppLanguage.TR
        return AppSettings(
            customMessage = prefs.getString(KEY_CUSTOM_MSG, "ACİL DURUM! Yardıma ihtiyacım var! Konumum: {location}") ?: "",
            countdownSeconds = prefs.getInt(KEY_COUNTDOWN, 3),
            sirenEnabled = prefs.getBoolean(KEY_SIREN, true),
            flashlightEnabled = prefs.getBoolean(KEY_FLASHLIGHT, true),
            vibrationEnabled = prefs.getBoolean(KEY_VIBRATION, true),
            autoCallPrimary = prefs.getBoolean(KEY_AUTO_CALL, true),
            emergencyDialNumber = prefs.getString(KEY_EMERGENCY_NUMBER, "112") ?: "112",
            testMode = prefs.getBoolean(KEY_TEST_MODE, false),
            language = lang
        )
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit()
            .putString(KEY_CUSTOM_MSG, settings.customMessage)
            .putInt(KEY_COUNTDOWN, settings.countdownSeconds)
            .putBoolean(KEY_SIREN, settings.sirenEnabled)
            .putBoolean(KEY_FLASHLIGHT, settings.flashlightEnabled)
            .putBoolean(KEY_VIBRATION, settings.vibrationEnabled)
            .putBoolean(KEY_AUTO_CALL, settings.autoCallPrimary)
            .putString(KEY_EMERGENCY_NUMBER, settings.emergencyDialNumber)
            .putBoolean(KEY_TEST_MODE, settings.testMode)
            .putString(KEY_LANGUAGE, settings.language.code)
            .apply()
    }

    fun loadContacts(): List<EmergencyContact> {
        val jsonString = prefs.getString(KEY_CONTACTS, null)
        if (jsonString.isNullOrEmpty()) {
            // Default pre-populated contacts
            val defaults = listOf(
                EmergencyContact(
                    name = "112 Acil Çağrı Merkezi",
                    phoneNumber = "112",
                    relationship = "Resmi Acil Servis",
                    isPrimary = true
                )
            )
            saveContacts(defaults)
            return defaults
        }

        val list = mutableListOf<EmergencyContact>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    EmergencyContact(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        name = obj.optString("name", ""),
                        phoneNumber = obj.optString("phone", ""),
                        relationship = obj.optString("rel", "Yakın"),
                        isPrimary = obj.optBoolean("isPrimary", false)
                    )
                )
            }
        } catch (_: Exception) {
            return emptyList()
        }
        return list
    }

    fun saveContacts(contacts: List<EmergencyContact>) {
        val array = JSONArray()
        for (c in contacts) {
            val obj = JSONObject().apply {
                put("id", c.id)
                put("name", c.name)
                put("phone", c.phoneNumber)
                put("rel", c.relationship)
                put("isPrimary", c.isPrimary)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_CONTACTS, array.toString()).apply()
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
    }
}
