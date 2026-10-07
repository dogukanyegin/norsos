package com.example.norsos.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.norsos.services.EmergencyDispatcher
import com.example.norsos.ui.EmergencyViewModel
import com.example.norsos.ui.theme.AlertAmber
import com.example.norsos.ui.theme.SosRed
import com.example.norsos.ui.theme.SosRedDark

@Composable
fun SosScreen(viewModel: EmergencyViewModel) {
    val context = LocalContext.current
    val isEmergencyActive by viewModel.isEmergencyActive.collectAsState()
    val isCountdownActive by viewModel.isCountdownActive.collectAsState()
    val countdownRemaining by viewModel.countdownRemaining.collectAsState()
    val isSirenPlaying by viewModel.isSirenPlaying.collectAsState()
    val isWhistlePlaying by viewModel.isWhistlePlaying.collectAsState()
    val isFlashlightStrobing by viewModel.isFlashlightStrobing.collectAsState()
    val locationData by viewModel.currentLocation.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val lang = settings.language
    val contacts by viewModel.contacts.collectAsState()
    val primaryContact = contacts.firstOrNull { it.isPrimary } ?: contacts.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Test mode warning banner
        if (settings.testMode) {
            item {
                Surface(
                    color = AlertAmber.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = AlertAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = com.example.norsos.localization.AppStrings.testModeBanner(lang),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Active Emergency Alert Card
        item {
            AnimatedVisibility(visible = isEmergencyActive) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SosRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_emergency_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = com.example.norsos.localization.AppStrings.activeEmergencyTitle(lang),
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = com.example.norsos.localization.AppStrings.activeEmergencyDesc(lang),
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.stopAllEmergencies() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = SosRedDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stop_emergency_button")
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(com.example.norsos.localization.AppStrings.stopEmergency(lang), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Main SOS Button Section
        item {
            SosButtonSection(
                isEmergencyActive = isEmergencyActive,
                isCountdownActive = isCountdownActive,
                countdownRemaining = countdownRemaining,
                countdownTotal = settings.countdownSeconds,
                lang = lang,
                onSosClick = { viewModel.onSosButtonPressed(context) },
                onCancelClick = { viewModel.cancelCountdown() }
            )
        }

        // Primary Contact Quick Call Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("primary_contact_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = com.example.norsos.localization.AppStrings.primaryContactLabel(lang),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = primaryContact?.name ?: "112 Acil Çağrı",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = primaryContact?.phoneNumber ?: "112",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row {
                        IconButton(
                            onClick = {
                                val number = primaryContact?.phoneNumber ?: "112"
                                EmergencyDispatcher.dialNumber(context, number)
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .testTag("quick_dial_button")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Ara", tint = Color.White)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                val number = primaryContact?.phoneNumber ?: "112"
                                val loc = locationData?.googleMapsUrl ?: "Konum alınamadı"
                                val msg = settings.customMessage.replace("{location}", loc)
                                EmergencyDispatcher.sendSms(context, number, msg)
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .testTag("quick_sms_button")
                        ) {
                            Icon(Icons.Default.Message, contentDescription = "SMS Gönder", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                    }
                }
            }
        }

        // Quick Safety Tools Grid (Whistle, Siren, Strobe, Share Location)
        item {
            Text(
                text = com.example.norsos.localization.AppStrings.quickToolsTitle(lang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickToolButton(
                    modifier = Modifier.weight(1f),
                    title = com.example.norsos.localization.AppStrings.toolWhistle(lang),
                    isActive = isWhistlePlaying,
                    icon = Icons.Default.Campaign,
                    onClick = { viewModel.toggleWhistle() }
                )
                QuickToolButton(
                    modifier = Modifier.weight(1f),
                    title = com.example.norsos.localization.AppStrings.toolSiren(lang),
                    isActive = isSirenPlaying,
                    icon = Icons.Default.Warning,
                    onClick = { viewModel.toggleSiren() }
                )
                QuickToolButton(
                    modifier = Modifier.weight(1f),
                    title = com.example.norsos.localization.AppStrings.toolStrobe(lang),
                    isActive = isFlashlightStrobing,
                    icon = Icons.Default.FlashOn,
                    onClick = { viewModel.toggleFlashlightStrobe() }
                )
                QuickToolButton(
                    modifier = Modifier.weight(1f),
                    title = com.example.norsos.localization.AppStrings.toolShare(lang),
                    isActive = false,
                    icon = Icons.Default.Share,
                    onClick = {
                        val loc = locationData?.googleMapsUrl ?: "Konum bilgisi bekleniyor"
                        val msg = settings.customMessage.replace("{location}", loc)
                        EmergencyDispatcher.shareText(context, "ACİL DURUM KONUMU", msg)
                    }
                )
            }
        }

        // Live Location Status Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("location_status_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = null,
                        tint = if (locationData != null) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (locationData != null) com.example.norsos.localization.AppStrings.liveLocationReady(lang) else com.example.norsos.localization.AppStrings.searchingGps(lang),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = locationData?.formattedCoordinates ?: "Açık alanda daha hızlı tespit edilir",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { viewModel.locationTracker.readLastKnownLocation() },
                        modifier = Modifier.testTag("refresh_location_button")
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Konumu Yenile")
                    }
                }
            }
        }
    }
}

@Composable
private fun SosButtonSection(
    isEmergencyActive: Boolean,
    isCountdownActive: Boolean,
    countdownRemaining: Int,
    countdownTotal: Int,
    lang: com.example.norsos.model.AppLanguage,
    onSosClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isEmergencyActive || isCountdownActive) 1.08f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(220.dp)
                .scale(pulseScale)
        ) {
            // Outer glow ring
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(
                        if (isEmergencyActive || isCountdownActive)
                            SosRed.copy(alpha = 0.25f)
                        else
                            SosRed.copy(alpha = 0.12f)
                    )
            )

            // Circular progress ring during countdown
            if (isCountdownActive && countdownTotal > 0) {
                CircularProgressIndicator(
                    progress = { countdownRemaining.toFloat() / countdownTotal.toFloat() },
                    modifier = Modifier.size(200.dp),
                    color = SosRed,
                    strokeWidth = 8.dp,
                    trackColor = Color.Transparent
                )
            }

            // Central Touch Button
            Surface(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .clickable { onSosClick() }
                    .border(
                        width = 4.dp,
                        color = Color.White.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .testTag("main_sos_button"),
                color = if (isEmergencyActive) SosRedDark else SosRed,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isCountdownActive) {
                        Text(
                            text = "$countdownRemaining",
                            color = Color.White,
                            fontSize = 60.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = com.example.norsos.localization.AppStrings.seconds(lang),
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (isEmergencyActive) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                        Text(
                            text = com.example.norsos.localization.AppStrings.stop(lang),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "S.O.S",
                            color = Color.White,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = com.example.norsos.localization.AppStrings.sosSubtitle(lang),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isCountdownActive) {
            OutlinedButton(
                onClick = onCancelClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SosRed),
                modifier = Modifier.testTag("cancel_countdown_button")
            ) {
                Icon(Icons.Default.Cancel, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(com.example.norsos.localization.AppStrings.cancelCountdown(lang), fontWeight = FontWeight.Bold)
            }
        } else {
            Text(
                text = if (isEmergencyActive) com.example.norsos.localization.AppStrings.instructionActive(lang) else com.example.norsos.localization.AppStrings.instructionIdle(lang),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuickToolButton(
    modifier: Modifier = Modifier,
    title: String,
    isActive: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isActive) SosRed else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
