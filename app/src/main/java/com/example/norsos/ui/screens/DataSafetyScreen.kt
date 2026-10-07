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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.norsos.model.AppLanguage
import com.example.norsos.ui.theme.EmergencyGreen

@Composable
fun DataSafetyScreen(lang: AppLanguage = AppLanguage.TR) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = when (lang) {
                    AppLanguage.TR -> "Gizlilik ve Veri Güvenliği"
                    AppLanguage.EN -> "Privacy & Data Safety"
                    AppLanguage.NO -> "Personvern og datasikkerhet"
                    AppLanguage.SV -> "Integritet och datasäkerhet"
                    AppLanguage.DA -> "Privatliv og datasikkerhed"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = when (lang) {
                    AppLanguage.TR -> "Google Play Geliştirici Politikaları & Veri Güvenliği Bildirimi"
                    AppLanguage.EN -> "Google Play Developer Policies & Data Safety Declaration"
                    AppLanguage.NO -> "Google Play-retningslinjer og datasikkerhetserklæring"
                    AppLanguage.SV -> "Google Play-policyer och datasäkerhetsdeklaration"
                    AppLanguage.DA -> "Google Play-politikker og datasikkerhedserklæring"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Summary Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmergencyGreen.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EmergencyGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = when (lang) {
                                AppLanguage.TR -> "Verileriniz %100 Cihazınızda Kalır"
                                AppLanguage.EN -> "Your Data Stays 100% on Device"
                                AppLanguage.NO -> "Dine data forblir 100% på enheten"
                                AppLanguage.SV -> "Dina data stannar 100% på enheten"
                                AppLanguage.DA -> "Dine data forbliver 100% på enheden"
                            },
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when (lang) {
                                AppLanguage.TR -> "Norsos hiçbir sunucuya kullanıcı verisi veya konum göndermez. Reklam kimliği veya izleyici içermez."
                                AppLanguage.EN -> "Norsos never transmits user data or location to any server. No ad IDs or trackers."
                                AppLanguage.NO -> "Norsos sender aldri brukerdata eller posisjon til noen server. Ingen sporere eller reklame-ID."
                                AppLanguage.SV -> "Norsos skickar aldrig användardata eller position till servrar. Inga spårare eller reklam-ID."
                                AppLanguage.DA -> "Norsos sender aldrig brugerdata eller placering til nogen server. Ingen sporere eller reklame-ID."
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Permissions Transparency
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = when (lang) {
                            AppLanguage.TR -> "İzin Kullanım Gerekçeleri"
                            AppLanguage.EN -> "Permission Justifications"
                            AppLanguage.NO -> "Tillatelsesbegrunnelser"
                            AppLanguage.SV -> "Behörighetsmotiveringar"
                            AppLanguage.DA -> "Tilladelsesbegrundelser"
                        },
                        fontWeight = FontWeight.Bold
                    )

                    PermissionItem(
                        title = when (lang) {
                            AppLanguage.TR -> "Konum İzni (ACCESS_FINE_LOCATION)"
                            AppLanguage.EN -> "Location Permission (ACCESS_FINE_LOCATION)"
                            AppLanguage.NO -> "Posisjonstillatelse (ACCESS_FINE_LOCATION)"
                            AppLanguage.SV -> "Platstillstånd (ACCESS_FINE_LOCATION)"
                            AppLanguage.DA -> "Placeringstilladelse (ACCESS_FINE_LOCATION)"
                        },
                        description = when (lang) {
                            AppLanguage.TR -> "Yalnızca acil durumda koordinatlarınızı acil kişilere göndermek için kullanılır."
                            AppLanguage.EN -> "Used solely to acquire coordinates for emergency dispatch. No background tracking."
                            AppLanguage.NO -> "Brukes utelukkende til å hente koordinater ved nødvarsling. Ingen bakgrunnssporing."
                            AppLanguage.SV -> "Används enbart för att hämta koordinater vid nödlarm. Ingen bakgrundsspårning."
                            AppLanguage.DA -> "Bruges udelukkende til at hente koordinater ved nødopkald. Ingen baggrundssporing."
                        }
                    )

                    PermissionItem(
                        title = when (lang) {
                            AppLanguage.TR -> "Kamera / Flaşör İzni (CAMERA)"
                            AppLanguage.EN -> "Camera / Flashlight (CAMERA)"
                            AppLanguage.NO -> "Kamera / Strobelys (CAMERA)"
                            AppLanguage.SV -> "Kamera / Ficklampa (CAMERA)"
                            AppLanguage.DA -> "Kamera / Lommelygte (CAMERA)"
                        },
                        description = when (lang) {
                            AppLanguage.TR -> "Karanlıkta dikkat çekmek için LED flaşı çakar fener olarak açıp kapatır. Fotoğraf çekilmez."
                            AppLanguage.EN -> "Toggles LED torch for emergency optical visibility. No photos or videos recorded."
                            AppLanguage.NO -> "Brukes kun til å blinke LED-lyset for nødsynlighet. Ingen bilder eller videoer tas."
                            AppLanguage.SV -> "Används endast för att blinka LED-blixten för nödsynlighet. Inga bilder tas."
                            AppLanguage.DA -> "Bruges kun til at blinke LED-lyset for nødsynlighed. Ingen billeder tages."
                        }
                    )

                    PermissionItem(
                        title = when (lang) {
                            AppLanguage.TR -> "Telefon Arama & SMS (Sistem Intentleri)"
                            AppLanguage.EN -> "Calls & SMS (System Intents)"
                            AppLanguage.NO -> "Anrop og SMS (System-intenter)"
                            AppLanguage.SV -> "Samtal och SMS (System-intenter)"
                            AppLanguage.DA -> "Opkald og SMS (System-intenter)"
                        },
                        description = when (lang) {
                            AppLanguage.TR -> "Standart ACTION_SENDTO ve ACTION_DIAL kullanılır. Riskli izinler istenmez."
                            AppLanguage.EN -> "Uses standard ACTION_SENDTO and ACTION_DIAL. No high-risk permissions required."
                            AppLanguage.NO -> "Bruker sikre standard ACTION_SENDTO og ACTION_DIAL-intenter."
                            AppLanguage.SV -> "Använder säkra standard ACTION_SENDTO och ACTION_DIAL-intenter."
                            AppLanguage.DA -> "Bruger sikre standard ACTION_SENDTO og ACTION_DIAL-intenter."
                        }
                    )
                }
            }
        }

        // Account & Data Deletion
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (lang) {
                                AppLanguage.TR -> "Hesap & Veri Silme Politikası"
                                AppLanguage.EN -> "Account & Data Erasure Policy"
                                AppLanguage.NO -> "Retningslinjer for sletting av data"
                                AppLanguage.SV -> "Policy för radering av data"
                                AppLanguage.DA -> "Politik for sletning af data"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (lang) {
                            AppLanguage.TR -> "1. Hesap Zorunluluğu Yoktur: Kayıt olmadan doğrudan kullanılır.\n2. Verilerin Silinmesi: Ayarlar sekmesindeki sıfırlama butonuyla tüm veriler anında silinir.\n3. Uygulama Kaldırma: Uygulama kaldırıldığında hiçbir kalıntı veri kalmaz."
                            AppLanguage.EN -> "1. No Account Required: Use immediately without sign-up.\n2. Data Erasure: Instantly wipe all data in Settings.\n3. Uninstall: No residual data remains on device."
                            AppLanguage.NO -> "1. Ingen konto kreves: Bruk umiddelbart uten registrering.\n2. Sletting: Slett alle data umiddelbart i Innstillinger.\n3. Avinstallering: Ingen restdata beholdes."
                            AppLanguage.SV -> "1. Inget konto krävs: Använd direkt utan registrering.\n2. Radering: Radera all data omedelbart i Inställningar.\n3. Avinstallation: Ingen data finns kvar."
                            AppLanguage.DA -> "1. Ingen konto påkrævet: Brug med det samme uden registrering.\n2. Sletning: Slet alle data med det samme i Indstillinger.\n3. Afinstallation: Ingen data bevares."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionItem(title: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = EmergencyGreen,
            modifier = Modifier.padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
