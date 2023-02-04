package io.github.alexeychurchill.stickynotes.contacts.presentation

import io.github.alexeychurchill.stickynotes.core.model.User

sealed interface SearchContactResult {
    object None : SearchContactResult
    object Loading : SearchContactResult
    object Empty : SearchContactResult
    data class Items(val items: List<Item>) : SearchContactResult {
        data class Item(
            val user: User,
            val isRequested: Boolean = false,
        ) {
            constructor(user: User) : this(
                user,
                isRequested = false,
            )
        }
    }
}
