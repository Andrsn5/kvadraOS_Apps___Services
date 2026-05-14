package dev.andre.kvadraos_apps___services.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.andre.kvadraos_apps___services.domain.repository.ContactRepository
import dev.andre.kvadraos_apps___services.presentation.state.ContactsState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ContactsState>(ContactsState.Idle)
    val state: StateFlow<ContactsState> = _state.asStateFlow()



    fun loadContacts() {
        viewModelScope.launch {
            _state.value = ContactsState.Loading
            repository.getContacts()
                .catch { e ->
                    if (e is CancellationException) throw e
                    _state.value = ContactsState.Failure(
                        e.message ?: "Неизвестная ошибка"
                    )
                }
                .collect { contacts ->
                    _state.value = ContactsState.Success(contacts)
                }
        }
    }
}
