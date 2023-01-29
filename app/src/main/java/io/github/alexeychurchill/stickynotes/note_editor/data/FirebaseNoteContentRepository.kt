package io.github.alexeychurchill.stickynotes.note_editor.data

import com.google.firebase.firestore.FirebaseFirestore
import io.github.alexeychurchill.stickynotes.core.model.NoteContent
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteContentRepository
import javax.inject.Inject

class FirebaseNoteContentRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : NoteContentRepository {

    override suspend fun getNoteContent(id: String): NoteContent? {
//        TODO("Not yet implemented")
        return null
    }

    private companion object {
        const val NOTE_CONTENT_PATH = "note_content"
    }
}
