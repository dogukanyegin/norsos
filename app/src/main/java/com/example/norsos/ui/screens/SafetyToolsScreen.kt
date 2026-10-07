package com.example.norsos.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.norsos.model.AppLanguage
import com.example.norsos.services.EmergencyDispatcher
import com.example.norsos.ui.EmergencyViewModel
import com.example.norsos.ui.theme.AlertAmber
import com.example.norsos.ui.theme.SosRed

data class EmergencyLine(
    val name: String,
    val number: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)

@Composable
fun SafetyToolsScreen(viewModel: EmergencyViewModel) {
    val context = LocalContext.current
    val isSirenPlaying by viewModel.isSirenPlaying.collectAsState()
    val isWhistlePlaying by viewModel.isWhistlePlaying.collectAsState()
    val isFlashlightStrobing by viewModel.isFlashlightStrobing.collectAsState()
    val locationData by viewModel.currentLocation.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val lang = settings.language

    val emergencyLines = when (lang) {
        AppLanguage.NO -> listOf(
            EmergencyLine("112 Politi", "112", "Politi og nødetater i Norge", Icons.Default.LocalPolice, Color(0xFF1565C0)),
            EmergencyLine("113 Ambulanse", "113", "Medisinsk nødhjelp og ambulanse", Icons.Default.MedicalServices, SosRed),
            EmergencyLine("110 Brannvesen", "110", "Brann, redning og ulykker", Icons.Default.LocalFireDepartment, Color(0xFFE65100)),
            EmergencyLine("116 117 Legevakt", "116117", "Nasjonalt legevaktnummer", Icons.Default.LocalHospital, Color(0xFF6A1B9A)),
            EmergencyLine("911 Internasjonalt", "911", "Internasjonal nødviderekobling", Icons.Default.Call, Color(0xFF37474F))
        )
        AppLanguage.SV -> listOf(
            EmergencyLine("112 SOS Alarm", "112", "Polis, ambulans och räddningstjänst i Sverige", Icons.Default.MedicalServices, SosRed),
            EmergencyLine("114 14 Polisen", "11414", "Polisen för icke-akuta ärenden", Icons.Default.LocalPolice, Color(0xFF1565C0)),
            EmergencyLine("1177 Vårdguiden", "1177", "Sjukvårdsrådgivning dygnet runt", Icons.Default.LocalHospital, Color(0xFF6A1B9A)),
            EmergencyLine("911 Internationellt", "911", "Internationell nödvidarekoppling", Icons.Default.Call, Color(0xFF37474F))
        )
        AppLanguage.DA -> listOf(
            EmergencyLine("112 Alarmcentralen", "112", "Politi, ambulance og brandvæsen i Danmark", Icons.Default.MedicalServices, SosRed),
            EmergencyLine("114 Politiet", "114", "Politi for ikke-akutte henvendelser", Icons.Default.LocalPolice, Color(0xFF1565C0)),
            EmergencyLine("1813 Akuttelefonen", "1813", "Lægevagt og sundhedsfaglig rådgivning", Icons.Default.LocalHospital, Color(0xFF6A1B9A)),
            EmergencyLine("911 Internationalt", "911", "International nødomstilling", Icons.Default.Call, Color(0xFF37474F))
        )
        AppLanguage.EN -> listOf(
            EmergencyLine("112 European Emergency", "112", "All emergency services (EU / International)", Icons.Default.MedicalServices, SosRed),
            EmergencyLine("911 US / International", "911", "Police, medical and fire dispatch", Icons.Default.LocalPolice, Color(0xFF1565C0)),
            EmergencyLine("999 UK Emergency", "999", "UK Emergency services", Icons.Default.LocalFireDepartment, Color(0xFFE65100))
        )
        AppLanguage.TR -> listOf(
            EmergencyLine("112 Acil Çağrı Merkezi", "112", "Tüm acil servisler (Ambulans, Polis, İtfaiye, Jandarma)", Icons.Default.MedicalServices, SosRed),
            EmergencyLine("155 Polis İmdat", "155", "Emniyet ve asayiş acil ihbar", Icons.Default.LocalPolice, Color(0xFF1565C0)),
            EmergencyLine("110 İtfaiye", "110", "Yangın, kurtarma ve kaza", Icons.Default.LocalFireDepartment, Color(0xFFE65100)),
            EmergencyLine("122 AFAD", "122", "Afet ve acil durum yönetimi", Icons.Default.Security, Color(0xFF2E7D32)),
            EmergencyLine("114 Zehir Danışma", "114", "Zehirlenme vakaları ve danışma", Icons.Default.LocalHospital, Color(0xFF6A1B9A)),
            EmergencyLine("911 Uluslararası", "911", "Uluslararası acil durum yönlendirmesi", Icons.Default.Call, Color(0xFF37474F))
        )
    }

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
                    AppLanguage.TR -> "Güvenlik Araçları & Rehber"
                    AppLanguage.EN -> "Safety Tools & Guidelines"
                    AppLanguage.NO -> "Sikkerhetsverktøy og guide"
                    AppLanguage.SV -> "Säkerhetsverktyg och guide"
                    AppLanguage.DA -> "Sikkerhedsværktøjer og guide"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = when (lang) {
                    AppLanguage.TR -> "Sesli ikazlar, tam koordinat tespiti ve resmi acil numaralar."
                    AppLanguage.EN -> "Audio-visual signals, live GPS location and official emergency lines."
                    AppLanguage.NO -> "Lyd- og lyssignaler, aktiv GPS og offisielle nødnumre."
                    AppLanguage.SV -> "Ljud- och ljussignaler, aktiv GPS och officiella nödnummer."
                    AppLanguage.DA -> "Lyd- og lyssignaler, aktiv GPS og officielle nødnumre."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Audio & Light Tools Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = when (lang) {
                            AppLanguage.TR -> "Ses ve Işık İkaz Araçları"
                            AppLanguage.EN -> "Sound & Light Warning Tools"
                            AppLanguage.NO -> "Lyd- og lysvarslingsverktøy"
                            AppLanguage.SV -> "Ljud- och ljusvarningsverktyg"
                            AppLanguage.DA -> "Lyd- og lysadvarselsværktøjer"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.toggleWhistle() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isWhistlePlaying) SosRed else MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                if (isWhistlePlaying) {
                                    when (lang) {
                                        AppLanguage.TR -> "Durdur"
                                        AppLanguage.EN -> "Stop Whistle"
                                        AppLanguage.NO -> "Stopp fløyte"
                                        AppLanguage.SV -> "Stoppa pipa"
                                        AppLanguage.DA -> "Stop fløjte"
                                    }
                                } else {
                                    when (lang) {
                                        AppLanguage.TR -> "Düdük Çal"
                                        AppLanguage.EN -> "Play Whistle"
                                        AppLanguage.NO -> "Blås i fløyte"
                                        AppLanguage.SV -> "Blås i visselpipa"
                                        AppLanguage.DA -> "Blæs i fløjte"
                                    }
                                },
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { viewModel.toggleSiren() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSirenPlaying) SosRed else AlertAmber
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                if (isSirenPlaying) {
                                    when (lang) {
                                        AppLanguage.TR -> "Sireni Durdur"
                                        AppLanguage.EN -> "Stop Siren"
                                        AppLanguage.NO -> "Stopp sirene"
                                        AppLanguage.SV -> "Stoppa siren"
                                        AppLanguage.DA -> "Stop sirene"
                                    }
                                } else {
                                    when (lang) {
                                        AppLanguage.TR -> "Siren Çal"
                                        AppLanguage.EN -> "Play Siren"
                                        AppLanguage.NO -> "Start sirene"
                                        AppLanguage.SV -> "Starta siren"
                                        AppLanguage.DA -> "Start sirene"
                                    }
                                },
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    FilledTonalButton(
                        onClick = { viewModel.toggleFlashlightStrobe() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.FlashOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (isFlashlightStrobing) {
                                when (lang) {
                                    AppLanguage.TR -> "Flaşörü Kapat"
                                    AppLanguage.EN -> "Turn Off Strobe"
                                    AppLanguage.NO -> "Slå av strobelys"
                                    AppLanguage.SV -> "Stäng av stroboskop"
                                    AppLanguage.DA -> "Sluk strobelys"
                                }
                            } else {
                                when (lang) {
                                    AppLanguage.TR -> "SOS Çakar Fenerini Aç"
                                    AppLanguage.EN -> "Turn On SOS Strobe Light"
                                    AppLanguage.NO -> "Slå på SOS-strobelys"
                                    AppLanguage.SV -> "Slå på SOS-stroboskop"
                                    AppLanguage.DA -> "Tænd SOS-strobelys"
                                }
                            }
                        )
                    }
                }
            }
        }

        // Live Coordinates & GPS Panel
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.TR -> "Canlı GPS Koordinatları"
                                    AppLanguage.EN -> "Live GPS Coordinates"
                                    AppLanguage.NO -> "Aktive GPS-koordinater"
                                    AppLanguage.SV -> "Aktiva GPS-koordinater"
                                    AppLanguage.DA -> "Aktive GPS-koordinater"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(onClick = { viewModel.locationTracker.readLastKnownLocation() }) {
                            Icon(Icons.Default.MyLocation, contentDescription = "Yenile")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (locationData != null) {
                        val loc = locationData!!
                        Text(
                            text = "${when(lang){AppLanguage.TR -> "Enlem"; AppLanguage.EN -> "Latitude"; AppLanguage.NO -> "Breddegrad"; AppLanguage.SV -> "Latitud"; AppLanguage.DA -> "Breddegrad"}}: ${String.format("%.6f", loc.latitude)}°",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${when(lang){AppLanguage.TR -> "Boylam"; AppLanguage.EN -> "Longitude"; AppLanguage.NO -> "Lengdegrad"; AppLanguage.SV -> "Longitud"; AppLanguage.DA -> "Længdegrad"}}: ${String.format("%.6f", loc.longitude)}°",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "±${loc.accuracy.toInt()} m",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Coordinates", "${loc.latitude}, ${loc.longitude}")
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Kopiert", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    when (lang) {
                                        AppLanguage.TR -> "Kopyala"
                                        AppLanguage.EN -> "Copy"
                                        AppLanguage.NO -> "Kopier"
                                        AppLanguage.SV -> "Kopiera"
                                        AppLanguage.DA -> "Kopier"
                                    },
                                    fontSize = 11.sp
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(loc.googleMapsUrl)).apply {
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    try {
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    when (lang) {
                                        AppLanguage.TR -> "Harita"
                                        AppLanguage.EN -> "Map"
                                        AppLanguage.NO -> "Kart"
                                        AppLanguage.SV -> "Karta"
                                        AppLanguage.DA -> "Kort"
                                    },
                                    fontSize = 11.sp
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    EmergencyDispatcher.shareText(
                                        context,
                                        "SOS Location",
                                        "${loc.googleMapsUrl} (${loc.formattedCoordinates})"
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    when (lang) {
                                        AppLanguage.TR -> "Paylaş"
                                        AppLanguage.EN -> "Share"
                                        AppLanguage.NO -> "Del"
                                        AppLanguage.SV -> "Dela"
                                        AppLanguage.DA -> "Del"
                                    },
                                    fontSize = 11.sp
                                )
                            }
                        }
                    } else {
                        Text(
                            text = when (lang) {
                                AppLanguage.TR -> "GPS sinyali aranıyor... Lütfen konum servislerini açın."
                                AppLanguage.EN -> "Searching for GPS signal... Please ensure location is enabled."
                                AppLanguage.NO -> "Søker etter GPS-signal... Sørg for at posisjonstjenester er på."
                                AppLanguage.SV -> "Söker efter GPS-signal... Se till att platstjänster är aktiverade."
                                AppLanguage.DA -> "Søger efter GPS-signal... Sørg for at placeringstjenester er slået til."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Official Emergency Lines Section
        item {
            Text(
                text = when (lang) {
                    AppLanguage.TR -> "Resmi Acil Numaralar (1-Dokunuş)"
                    AppLanguage.EN -> "Official Emergency Numbers (1-Tap)"
                    AppLanguage.NO -> "Offisielle nødnumre (1-trykk)"
                    AppLanguage.SV -> "Officiella nödnummer (1-tryck)"
                    AppLanguage.DA -> "Officielle nødnumre (1-tryk)"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(emergencyLines.size) { idx ->
            val line = emergencyLines[idx]
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { EmergencyDispatcher.dialNumber(context, line.number) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(line.color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(line.icon, contentDescription = null, tint = line.color, modifier = Modifier.size(22.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(line.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                        Text(line.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    IconButton(
                        onClick = { EmergencyDispatcher.dialNumber(context, line.number) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(line.color)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Ara", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Disaster Guidelines Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = when (lang) {
                            AppLanguage.TR -> "Afet & Acil Durum Rehberi"
                            AppLanguage.EN -> "Disaster & Emergency Guidelines"
                            AppLanguage.NO -> "Retningslinjer for krise og nødsituasjon"
                            AppLanguage.SV -> "Riktlinjer för kris och nödsituation"
                            AppLanguage.DA -> "Retningslinjer for krise og nødsituation"
                        },
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = when (lang) {
                            AppLanguage.TR -> "1. Deprem anında ÇÖKÜN, KAPANIN ve TUTUNUN.\n2. Enkaz altındaysanız enerjinizi koruyun ve Norsos düdüğünü periyodik çalın.\n3. Yangın durumunda dumanın altından eğilerek çıkış kapısına ilerleyin."
                            AppLanguage.EN -> "1. In an earthquake: Drop, Cover, and Hold on.\n2. If trapped: Preserve energy and sound the Norsos whistle periodically.\n3. In a fire: Stay low beneath smoke and move to the nearest emergency exit."
                            AppLanguage.NO -> "1. Ved jordskjelv/ras: Søk dekning under solide møbler og hold fast.\n2. Hvis du er innesperret: Spar på kreftene og bruk Norsos-fløyten med jevne mellomrom.\n3. Ved brann: Hold deg lavt under røyken og finn nærmeste nødutgang."
                            AppLanguage.SV -> "1. Vid jordskalv/ras: Sök skydd under stabila möbler och håll i dig.\n2. Om du är instängd: Spara energi och använd Norsos-visselpipan regelbundet.\n3. Vid brand: Håll dig lågt under röken och ta dig till närmaste nödutgång."
                            AppLanguage.DA -> "1. Ved jordskælv/ulykke: Søg dækning under solide møbler og hold fast.\n2. Hvis du er indespærret: Spar på kræfterne og brug Norsos-fløjten regelmæssigt.\n3. Ved brand: Hold dig lavt under røgen og find nærmeste nødudgang."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
