package dev.andre.kvadraos_apps___services.presentation.state

import dev.andre.kvadraos_apps___services.domain.model.Contact

sealed interface ContactsState {
    data object Idle : ContactsState
    data object Loading : ContactsState
    data class Success(val contacts: List<Contact>) : ContactsState
    data class Failure(val message: String) : ContactsState
}