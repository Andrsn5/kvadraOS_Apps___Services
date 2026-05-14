package dev.andre.kvadraos_apps___services.service

import android.app.Service
import android.content.ContentProviderOperation
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import dev.andre.kvadraos_apps___services.IContactService
import dev.andre.kvadraos_apps___services.presentation.util.normalizePhone
import timber.log.Timber

class ContactService : Service() {
    private val binder = object : IContactService.Stub() {
        override fun deleteDuplicateContacts(): Int {
            return findAndDeleteDuplicates()
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private fun findAndDeleteDuplicates(): Int {
        val contacts = loadContacts()
        val duplicates = mutableListOf<Long>()

        contacts.groupBy { "${it.name.lowercase()}|${normalizePhone(it.phone)}" }
            .values
            .forEach { group ->
                if (group.size > 1) {
                    val ids = group
                        .sortedBy { it.rawContactId }
                        .drop(1)
                        .map { it.rawContactId }

                    Timber.d("duplicates found -> $ids")
                    duplicates.addAll(ids)
                }
            }
        return deleteContacts(duplicates)
    }

    private fun loadContacts(): List<ContactData> {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        ) ?: return emptyList()

        return try {
            val result = mutableListOf<ContactData>()
            while (cursor.moveToNext()) {
                val rawId = cursor.getLong(0)

                val name = cursor.getString(1)
                    ?.trim()
                    .orEmpty()

                val phone = normalizePhone(
                    cursor.getString(2)
                        ?.trim()
                        .orEmpty()
                )

                if (name.isEmpty() || phone.isEmpty()) continue
                result.add(
                    ContactData(
                        rawContactId = rawId,
                        name = name,
                        phone = phone
                    )
                )
            }
            result
        } finally {
            cursor.close()
        }
    }

    private fun deleteContacts(rawContactIds: List<Long>): Int {
        if (rawContactIds.isEmpty()) return 0
        val operations = rawContactIds.mapTo(arrayListOf()) { id ->
            ContentProviderOperation
                .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(
                    "${ContactsContract.RawContacts._ID}=?",
                    arrayOf(id.toString())
                )
                .build()
        }
        return runCatching {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            rawContactIds.size
        }.getOrDefault(0)
    }

    data class ContactData(
        val rawContactId: Long,
        val name: String,
        val phone: String
    )
}