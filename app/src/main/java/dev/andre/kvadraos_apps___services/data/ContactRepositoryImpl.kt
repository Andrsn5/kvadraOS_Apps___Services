package dev.andre.kvadraos_apps___services.data

import android.content.ContentResolver
import android.database.ContentObserver
import android.provider.ContactsContract
import dev.andre.kvadraos_apps___services.domain.model.Contact
import dev.andre.kvadraos_apps___services.domain.repository.ContactRepository
import dev.andre.kvadraos_apps___services.presentation.util.normalizePhone
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val dispatcher: CoroutineDispatcher
) : ContactRepository {
    override fun getContacts(): Flow<List<Contact>> = callbackFlow {
        suspend fun loadAndSend() {
            try {
                val contacts = withContext(dispatcher) {
                    fetchContacts()
                }
                trySend(contacts)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                trySend(emptyList())
            }
        }

        val observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                launch {
                    loadAndSend()
                }
            }
        }

        contentResolver.registerContentObserver(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            true,
            observer
        )
        loadAndSend()
        awaitClose {
            contentResolver.unregisterContentObserver(observer)
        }
    }
        .distinctUntilChanged()
        .flowOn(dispatcher)

    private fun fetchContacts(): List<Contact> {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        ) ?: return emptyList()

        return try {
            val map = mutableMapOf<String, Contact>()

            while (cursor.moveToNext()) {
                val id = cursor.getString(0)
                    ?.trim()
                    .orEmpty()

                val name = cursor.getString(1)
                    ?.trim()
                    .orEmpty()

                val number = normalizePhone(
                    cursor.getString(2)
                        ?.trim()
                        .orEmpty()
                )

                val type = cursor.getInt(3)

                val photoUri = cursor.getString(4)
                    ?.trim()
                if (
                    id.isEmpty() ||
                    name.isEmpty() ||
                    number.isEmpty()
                ) {
                    continue
                }

                val isMobile =
                    type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE

                if (!map.containsKey(id) || isMobile) {
                    map[id] = Contact(
                        id = id,
                        displayName = name,
                        phoneNumber = number,
                        photoUri = photoUri
                    )
                }
            }
            map.values.toList()
        } finally {
            cursor.close()
        }
    }
}