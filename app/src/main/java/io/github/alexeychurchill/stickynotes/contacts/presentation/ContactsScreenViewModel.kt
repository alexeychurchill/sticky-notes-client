package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactScreenMode.CONTACTS
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactScreenMode.REQUESTS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsScreenViewModel @Inject constructor() : ViewModel() {
    private val allTabs = listOf(CONTACTS, REQUESTS)

    private val _mode = MutableStateFlow(CONTACTS)

    val mode: StateFlow<ContactScreenMode>
        get() = _mode

    val state: StateFlow<ContactScreenModeState>
        get() = _mode
            .map { mode -> ContactScreenModeState(allTabs, mode) }
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = ContactScreenModeState()
            )

    fun onSelectMode(mode: ContactScreenMode) {
        viewModelScope.launch {
            _mode.emit(mode)
        }
    }
}
