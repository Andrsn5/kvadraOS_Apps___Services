package dev.andre.kvadraos_apps___services.presentation

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.andre.kvadraos_apps___services.IContactService
import dev.andre.kvadraos_apps___services.R
import dev.andre.kvadraos_apps___services.domain.repository.ContactRepository
import dev.andre.kvadraos_apps___services.presentation.state.ContactsState
import dev.andre.kvadraos_apps___services.presentation.util.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state: MutableStateFlow<ContactsState> = MutableStateFlow(ContactsState.Idle)
    val state: StateFlow<ContactsState> = _state.asStateFlow()

    private var service: IContactService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        @SuppressLint("StringFormatMatches")
        override fun onServiceConnected(
            name: ComponentName?,
            binder: IBinder?
        ) {
            service = IContactService.Stub.asInterface(binder)
            isBound = true
            viewModelScope.launch {
                runCatching {
                    delay(300)
                    val deleted = service?.deleteDuplicateContacts() ?: 0
                    Timber.d("deleted duplicates = $deleted")
                    delay(300)

                    if (deleted > 0) {
                        showToast(context, context.getString(R.string.duplicates_deleted, deleted))
                    } else {
                        showToast(context, context.getString(R.string.duplicates_not_found))
                    }
                }.onFailure { error ->
                    Timber.e(error, "Error deleting duplicates")
                    showToast(context, context.getString(R.string.duplicates_deleted_failed))
                }.also {
                    unbindService()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            isBound = false
        }
    }

    fun loadContacts() {
        viewModelScope.launch {
            repository.getContacts().collect { contacts ->
                _state.update { ContactsState.Success(contacts) }
            }
        }
    }

    fun deleteDuplicateContacts() {
        if (isBound) {
            showToast(context, context.getString(R.string.delete_processing))
            return}
        val intent = Intent(
            context,
            dev.andre.kvadraos_apps___services.service.ContactService::class.java
        )
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        if (!isBound) return
        runCatching {
            context.unbindService(connection)
        }.also {
            isBound = false
            service = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        unbindService()
    }
}