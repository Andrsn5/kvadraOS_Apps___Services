package dev.andre.kvadraos_apps___services.domain.repository

import dev.andre.kvadraos_apps___services.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getContacts(): Flow<List<Contact>>
}
