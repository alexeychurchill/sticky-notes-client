@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.account.domain.UserRepository
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String>
        get() = _searchQuery

    val searchResults: StateFlow<SearchContactResult> by lazy {
        _searchQuery
            .flatMapLatest { query ->
                if (query.isEmpty()) {
                    return@flatMapLatest flowOf<SearchContactResult>(
                        SearchContactResult.None
                    )
                }

                searchBy(query)
            }
            .flowOn(dispatchers.io)
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = SearchContactResult.None,
            )
    }

    fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }

    fun makeRequest(userId: String) {
        viewModelScope.launch {
            userRepository.makeRequest(userId)
        }
    }

    private suspend fun searchBy(query: String) = userRepository
        .outgoingRequests()
        .transformLatest { outgoing ->
            emit(SearchContactResult.Loading)
            val outgoingSet = outgoing.map(User::id).toSet()
            val found = userRepository.searchUser(query)
                .map { user ->
                    SearchContactResult.Items.Item(
                        user = user,
                        isRequested = outgoingSet.contains(user.id),
                    )
                }
            emit(SearchContactResult.Items(found))
        }
}
