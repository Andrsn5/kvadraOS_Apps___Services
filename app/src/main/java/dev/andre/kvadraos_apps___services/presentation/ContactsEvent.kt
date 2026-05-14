package dev.andre.kvadraos_apps___services.presentation

sealed interface ContactsEvent {
    data class NavigateToCall(val phone: String) : ContactsEvent
}
