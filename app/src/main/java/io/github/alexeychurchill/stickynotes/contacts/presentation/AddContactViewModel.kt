@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.account.domain.UserRepository
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
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
            .transformLatest { query -> processSearchFlow(query) }
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

    private suspend fun FlowCollector<SearchContactResult>.processSearchFlow(
        query: String
    ) {
        if (query.isEmpty()) {
            emit(SearchContactResult.None)
            return
        }

        emit(SearchContactResult.Loading)
        val results = userRepository.searchUser(query)
        emit(SearchContactResult.Items(results))
    }
}
