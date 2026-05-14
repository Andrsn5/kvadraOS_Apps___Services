package dev.andre.kvadraos_apps___services.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import dev.andre.kvadraos_apps___services.domain.model.Contact
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun ContactList(
    contacts: List<Contact>,
    onItemClick: (Contact) -> Unit
) {
    val grouped = contacts.groupBy {
        it.displayName.trim().firstOrNull()?.uppercaseChar() ?: '#'
    }
    LazyColumn(
        contentPadding = WindowInsets.systemBars.asPaddingValues()
    ) {
        grouped.forEach { (letter, group) ->
            stickyHeader {
                ContactHeader(letter)
            }
            items(group, key = {it.id}) { contact ->
                ContactItem(contact, onItemClick)
            }
        }
    }
}