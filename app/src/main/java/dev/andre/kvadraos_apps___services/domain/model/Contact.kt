package dev.andre.kvadraos_apps___services.domain.model

data class Contact(
    val id: String,
    val displayName: String,
    val phoneNumber: String,
    val photoUri: String? = null
)
