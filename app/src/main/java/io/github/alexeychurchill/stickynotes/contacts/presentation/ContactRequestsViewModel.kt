package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactRequestsViewModel @Inject constructor() : ViewModel() {

    val isLoading: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    val incomingRequests: StateFlow<List<User>>
        get() {
            return MutableStateFlow(
                listOf(
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Jagadisha",
                        lastName = "Saturn",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Vishnu",
                        lastName = "Marharyta",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Nikandr",
                        lastName = "Larunda",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Jagadisha",
                        lastName = "Saturn",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Vishnu",
                        lastName = "Marharyta",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Nikandr",
                        lastName = "Larunda",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Jagadisha",
                        lastName = "Saturn",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Vishnu",
                        lastName = "Marharyta",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Nikandr",
                        lastName = "Larunda",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Jagadisha",
                        lastName = "Saturn",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Vishnu",
                        lastName = "Marharyta",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Nikandr",
                        lastName = "Larunda",
                    ),
                )
            )
        }

    val outcomingRequests: StateFlow<List<User>>
        get() {
            return MutableStateFlow(
                listOf(
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                    User(
                        id = "1",
                        login = "qwe",
                        firstName = "Danyil",
                        lastName = "Wolodymyr",
                    ),
                    User(
                        id = "2",
                        login = "rtyuiop",
                        firstName = "Mykhailo",
                        lastName = "Pramoda",
                    ),
                    User(
                        id = "3",
                        login = "asdfg",
                        firstName = "Shiva",
                        lastName = "Milana",
                    ),
                )
            )
        }

    fun acceptIncoming(userId: String) {
        // TODO: Implement accepting
    }

    fun rejectIncoming(userId: String) {
        // TODO: Implement rejecting
    }

    fun cancelOutcoming(userId: String) {
        // TODO: Implement outcoming request cancellation
    }
}
