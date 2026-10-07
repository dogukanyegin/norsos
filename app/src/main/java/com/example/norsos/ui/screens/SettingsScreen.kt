package com.example.norsos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.norsos.localization.AppStrings
import com.example.norsos.model.AppLanguage
import com.example.norsos.ui.EmergencyViewModel
import com.example.norsos.ui.NavTab
import com.example.norsos.ui.theme.SosRed

@Composable
fun SettingsScreen(viewModel: EmergencyViewModel) {
    val settings by viewModel.settings.collectAsState()
    val lang = settings.language
    var customMsg by remember(settings.customMessage) { mutableStateOf(settings.customMessage) }
    var showResetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = AppStrings.settingsTitle(lang),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = AppStrings.settingsSubtitle(lang),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Language Selector Card (Türkçe, English, Norsk, Svenska, Dansk)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().testTag("language_selection_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.languageSelectionTitle(lang),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AppLanguage.entries.forEach { itemLang ->
                            FilterChip(
                                selected = lang == itemLang,
                                onClick = { viewModel.setLanguage(itemLang) },
                                label = { Text("${itemLang.flag} ${itemLang.displayName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SosRed,
                                    selectedLabelColor = androidx.compose.ui.graphics.Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Custom SMS Message
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(AppStrings.messageTemplateTitle(lang), fontWeight = FontWeight.Bold)
                    Text(
                        text = AppStrings.messageTemplateHint(lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customMsg,
                        onValueChange = {
                            customMsg = it
                            viewModel.updateSettings(settings.copy(customMessage = it))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_message_input"),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }
        }

        // Countdown duration selector
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(AppStrings.countdownTitle(lang), fontWeight = FontWeight.Bold)
                    Text(
                        text = AppStrings.countdownDesc(lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0 to "0s", 3 to "3s", 5 to "5s", 10 to "10s").forEach { (sec, label) ->
                            FilterChip(
                                selected = settings.countdownSeconds == sec,
                                onClick = { viewModel.updateSettings(settings.copy(countdownSeconds = sec)) },
                                label = { Text(label, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }
        }

        // Trigger action toggles
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(AppStrings.triggerBehaviors(lang), fontWeight = FontWeight.Bold)

                    SettingToggleRow(
                        title = AppStrings.sirenAlarm(lang),
                        subtitle = AppStrings.sirenAlarmDesc(lang),
                        checked = settings.sirenEnabled,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(sirenEnabled = it)) }
                    )

                    SettingToggleRow(
                        title = AppStrings.strobeLight(lang),
                        subtitle = AppStrings.strobeLightDesc(lang),
                        checked = settings.flashlightEnabled,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(flashlightEnabled = it)) }
                    )

                    SettingToggleRow(
                        title = AppStrings.vibrationHaptic(lang),
                        subtitle = AppStrings.vibrationHapticDesc(lang),
                        checked = settings.vibrationEnabled,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(vibrationEnabled = it)) }
                    )

                    SettingToggleRow(
                        title = AppStrings.autoCallPrimary(lang),
                        subtitle = AppStrings.autoCallPrimaryDesc(lang),
                        checked = settings.autoCallPrimary,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(autoCallPrimary = it)) }
                    )

                    SettingToggleRow(
                        title = AppStrings.testMode(lang),
                        subtitle = AppStrings.testModeDesc(lang),
                        checked = settings.testMode,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(testMode = it)) }
                    )
                }
            }
        }

        // Privacy & Data Safety Nav Button
        item {
            OutlinedButton(
                onClick = { viewModel.setTab(NavTab.DATA_SAFETY) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PrivacyTip, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.tabDataSafety(lang))
            }
        }

        // Reset Data Button
        item {
            OutlinedButton(
                onClick = { showResetDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = SosRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DeleteSweep, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    when (lang) {
                        AppLanguage.TR -> "Tüm Verileri Sıfırla"
                        AppLanguage.EN -> "Reset All Data"
                        AppLanguage.NO -> "Tilbakestill alle data"
                        AppLanguage.SV -> "Återställ all data"
                        AppLanguage.DA -> "Nulstil alle data"
                    }
                )
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = SosRed) },
            title = {
                Text(
                    when (lang) {
                        AppLanguage.TR -> "Tüm Veriler Sıfırlansın mı?"
                        AppLanguage.EN -> "Reset All Data?"
                        AppLanguage.NO -> "Tilbakestill alle data?"
                        AppLanguage.SV -> "Återställ all data?"
                        AppLanguage.DA -> "Nulstil alle data?"
                    }
                )
            },
            text = {
                Text(
                    when (lang) {
                        AppLanguage.TR -> "Kayıtlı tüm acil kişileriniz ve ayarlarınız cihazınızdan tamamen silinecektir."
                        AppLanguage.EN -> "All saved emergency contacts and settings will be permanently removed from this device."
                        AppLanguage.NO -> "Alle lagrede nødkontakter og innstillinger blir slettet permanent fra enheten."
                        AppLanguage.SV -> "Alla sparade nödkontakter och inställningar raderas permanent från enheten."
                        AppLanguage.DA -> "Alle gemte nødkontakter og indstillinger slettes permanent fra enheden."
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showResetDialog = false
                    }
                ) {
                    Text(
                        when (lang) {
                            AppLanguage.TR -> "Evet, Sıfırla"
                            AppLanguage.EN -> "Yes, Reset"
                            AppLanguage.NO -> "Ja, tilbakestill"
                            AppLanguage.SV -> "Ja, återställ"
                            AppLanguage.DA -> "Ja, nulstil"
                        },
                        color = SosRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(AppStrings.cancel(lang))
                }
            }
        )
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
