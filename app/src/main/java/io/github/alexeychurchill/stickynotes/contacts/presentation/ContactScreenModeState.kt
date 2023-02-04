package io.github.alexeychurchill.stickynotes.contacts.presentation

data class ContactScreenModeState(
    val all: List<ContactScreenMode> = emptyList(),
    val selected: ContactScreenMode? = null,
)
