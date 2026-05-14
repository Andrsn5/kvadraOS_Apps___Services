package dev.andre.kvadraos_apps___services.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.andre.kvadraos_apps___services.presentation.util.launchDialIntent
import dev.andre.kvadraos_apps___services.presentation.state.ContactsEmpty
import dev.andre.kvadraos_apps___services.presentation.state.ContactsError
import dev.andre.kvadraos_apps___services.presentation.state.ContactsLoading
import dev.andre.kvadraos_apps___services.presentation.state.ContactsState

@Composable
fun ContactsScreen(viewModel: ContactsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadContacts()
        }
    }

    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            viewModel.loadContacts()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is ContactsEvent.NavigateToCall ->
                    launchDialIntent(context, event.phone)
            }
        }
    }

    when (val s = state) {
        is ContactsState.Loading -> ContactsLoading()
        is ContactsState.Success -> {
            if (s.contacts.isEmpty()) ContactsEmpty()
            else ContactList(s.contacts, viewModel.onContactClick)
        }
        is ContactsState.Failure -> ContactsError(s.message)
        is ContactsState.Idle -> ContactsLoading()
    }
}

@Preview
@Composable
fun ContactsScreenPreview() {
    ContactsScreen()
}


