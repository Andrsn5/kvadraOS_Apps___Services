package dev.andre.kvadraos_apps___services.data

import android.content.ContentResolver
import dev.andre.kvadraos_apps___services.domain.model.Contact
import dev.andre.kvadraos_apps___services.domain.repository.ContactRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ContactRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val dispatcher: CoroutineDispatcher
) : ContactRepository {

    override fun getContacts(): Flow<List<Contact>> = callbackFlow {
        try {
            val contacts = withContext(dispatcher) { fetchContacts() }
            trySend(contacts)
        } catch (e: CancellationException) {
            throw e
        } catch (e: SecurityException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(emptyList())
        }
        close()
    }.flowOn(dispatcher)

    private fun fetchContacts(): List<Contact> {
        return emptyList()
    }
}