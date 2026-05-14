package dev.andre.kvadraos_apps___services.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.andre.kvadraos_apps___services.domain.model.Contact
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun ContactList(
    contacts: List<Contact>
) {
    val grouped = contacts.groupBy {
        it.displayName.trim().firstOrNull()?.uppercaseChar() ?: '#'
    }
    LazyColumn {
        grouped.forEach { (letter, group) ->
            stickyHeader {
                ContactHeader(letter)
            }
            items(group, key = {it.id}) { contact ->
                ContactItem(contact)
            }
        }
    }
}

@Preview
@Composable
fun ContactListPreview() {
    ContactList(
        listOf(
            Contact(
                id = 1.toString(),
                displayName = "Contact 1",
                phoneNumber = "1234567890",
            )
        ))
}