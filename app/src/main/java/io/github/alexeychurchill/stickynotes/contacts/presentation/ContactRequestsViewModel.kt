package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactRequestsViewModel @Inject constructor() : ViewModel() {

    val isOutcomingLoading: StateFlow<Boolean>
        get() = MutableStateFlow(false)

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
                )
            )
        }

    fun cancelOutcomingRequest(user: User) {
        // TODO: Implement outcoming request cancellation
    }
}
