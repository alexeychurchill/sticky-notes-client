package io.github.alexeychurchill.stickynotes.contacts.presentation

import io.github.alexeychurchill.stickynotes.core.model.User

sealed interface SearchContactResult {
    object None : SearchContactResult
    object Loading : SearchContactResult
    object Empty : SearchContactResult
    data class Items(val items: List<User>) : SearchContactResult
}
