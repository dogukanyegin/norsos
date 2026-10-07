package com.example.norsos

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.example.norsos.ui.EmergencyViewModel
import com.example.norsos.ui.NavTab
import com.example.norsos.ui.screens.ContactsScreen
import com.example.norsos.ui.screens.DataSafetyScreen
import com.example.norsos.ui.screens.SafetyToolsScreen
import com.example.norsos.ui.screens.SettingsScreen
import com.example.norsos.ui.screens.SosScreen
import com.example.norsos.ui.theme.NorsosTheme
import com.example.norsos.ui.theme.SosRed

class MainActivity : ComponentActivity() {

    private val viewModel: EmergencyViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NorsosTheme {
                val activeTab by viewModel.activeTab.collectAsState()
                val settings by viewModel.settings.collectAsState()
                val lang = settings.language
                var showLangMenu by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

                // Permission launcher for Location and Camera
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) {
                    viewModel.locationTracker.readLastKnownLocation()
                }

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA
                        )
                    )
                }

                // Handle system back navigation to return to SOS screen
                BackHandler(enabled = activeTab != NavTab.SOS) {
                    viewModel.setTab(NavTab.SOS)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (activeTab) {
                                        NavTab.SOS -> com.example.norsos.localization.AppStrings.appTitle(lang)
                                        NavTab.CONTACTS -> com.example.norsos.localization.AppStrings.tabContacts(lang)
                                        NavTab.TOOLS -> com.example.norsos.localization.AppStrings.tabTools(lang)
                                        NavTab.SETTINGS -> com.example.norsos.localization.AppStrings.tabSettings(lang)
                                        NavTab.DATA_SAFETY -> com.example.norsos.localization.AppStrings.tabDataSafety(lang)
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            actions = {
                                androidx.compose.foundation.layout.Box {
                                    androidx.compose.material3.TextButton(
                                        onClick = { showLangMenu = true },
                                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                                            contentColor = if (activeTab == NavTab.SOS) Color.White else MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(
                                            text = "${lang.flag} ${lang.code.uppercase()}",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    androidx.compose.material3.DropdownMenu(
                                        expanded = showLangMenu,
                                        onDismissRequest = { showLangMenu = false }
                                    ) {
                                        com.example.norsos.model.AppLanguage.entries.forEach { itemLang ->
                                            androidx.compose.material3.DropdownMenuItem(
                                                text = { Text("${itemLang.flag} ${itemLang.displayName}") },
                                                onClick = {
                                                    viewModel.setLanguage(itemLang)
                                                    showLangMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = if (activeTab == NavTab.SOS) SosRed else Color.Transparent,
                                titleContentColor = if (activeTab == NavTab.SOS) Color.White else Color.Unspecified
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(modifier = Modifier.testTag("bottom_nav_bar")) {
                            NavigationBarItem(
                                selected = activeTab == NavTab.SOS,
                                onClick = { viewModel.setTab(NavTab.SOS) },
                                icon = { Icon(Icons.Default.Emergency, contentDescription = "SOS") },
                                label = { Text(com.example.norsos.localization.AppStrings.tabSos(lang)) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SosRed,
                                    selectedTextColor = SosRed
                                ),
                                modifier = Modifier.testTag("nav_sos")
                            )

                            NavigationBarItem(
                                selected = activeTab == NavTab.CONTACTS,
                                onClick = { viewModel.setTab(NavTab.CONTACTS) },
                                icon = { Icon(Icons.Default.People, contentDescription = "Kişiler") },
                                label = { Text(com.example.norsos.localization.AppStrings.tabContacts(lang)) },
                                modifier = Modifier.testTag("nav_contacts")
                            )

                            NavigationBarItem(
                                selected = activeTab == NavTab.TOOLS,
                                onClick = { viewModel.setTab(NavTab.TOOLS) },
                                icon = { Icon(Icons.Default.Build, contentDescription = "Araçlar") },
                                label = { Text(com.example.norsos.localization.AppStrings.tabTools(lang)) },
                                modifier = Modifier.testTag("nav_tools")
                            )

                            NavigationBarItem(
                                selected = activeTab == NavTab.SETTINGS || activeTab == NavTab.DATA_SAFETY,
                                onClick = { viewModel.setTab(NavTab.SETTINGS) },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Ayarlar") },
                                label = { Text(com.example.norsos.localization.AppStrings.tabSettings(lang)) },
                                modifier = Modifier.testTag("nav_settings")
                            )
                        }
                    }
                ) { innerPadding ->
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (activeTab) {
                            NavTab.SOS -> SosScreen(viewModel = viewModel)
                            NavTab.CONTACTS -> ContactsScreen(viewModel = viewModel)
                            NavTab.TOOLS -> SafetyToolsScreen(viewModel = viewModel)
                            NavTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                            NavTab.DATA_SAFETY -> DataSafetyScreen(lang = lang)
                        }
                    }
                }
            }
        }
    }
}
