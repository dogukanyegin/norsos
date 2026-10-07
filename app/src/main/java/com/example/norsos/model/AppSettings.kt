package com.example.norsos.model

data class AppSettings(
    val customMessage: String = "ACİL DURUM! Yardıma ihtiyacım var! Konumum: {location}",
    val countdownSeconds: Int = 3,
    val sirenEnabled: Boolean = true,
    val flashlightEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val autoCallPrimary: Boolean = true,
    val emergencyDialNumber: String = "112",
    val testMode: Boolean = false,
    val language: AppLanguage = AppLanguage.TR
)
