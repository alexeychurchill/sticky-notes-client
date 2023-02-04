package io.github.alexeychurchill.stickynotes.notes.domain

import io.github.alexeychurchill.stickynotes.account.AccountRepository
import io.github.alexeychurchill.stickynotes.account.AuthState.Authorized
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry
import io.github.alexeychurchill.stickynotes.core.model.NoteEntry.Companion.NO_ID
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class NoteEntryFactory @Inject constructor(
    private val accountRepository: AccountRepository,
) {
    suspend fun create(title: String): NoteEntry {
        val user = accountRepository.authState
            .filterIsInstance<Authorized>()
            .first()
            .user
        val now = Calendar.getInstance()
        return NoteEntry(
            id = NO_ID,
            ownerId = user.id,
            title = title,
            subject = null,
            changedAt = now,
        )
    }
}
