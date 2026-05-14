package dev.andre.kvadraos_apps___services.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.andre.kvadraos_apps___services.R
import dev.andre.kvadraos_apps___services.domain.model.Contact
import dev.andre.kvadraos_apps___services.presentation.util.buildInitials

@Composable
fun ContactItem(contact: Contact, onClick: (Contact) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(contact) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactAvatar(
            initials = buildInitials(contact.displayName),
            photoUri = contact.photoUri
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = contact.displayName.trim().ifEmpty {
                    stringResource(R.string.contact_name_stub)
                },
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = contact.phoneNumber.trim(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}