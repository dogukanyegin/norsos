package com.example.norsos.model

data class EmergencyContact(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val phoneNumber: String,
    val relationship: String = "Yakın",
    val isPrimary: Boolean = false
)
