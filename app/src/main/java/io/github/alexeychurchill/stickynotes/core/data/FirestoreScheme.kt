package io.github.alexeychurchill.stickynotes.core.data

object FirestoreScheme {
    object NoteEntry {
        const val Path = "note_entries"
        object Fields {
            const val Title = "title"
            const val Subject = "subject"
            const val OwnerId = "ownerId"
            const val ChangedAt = "changedAt"
            const val Content = "contentRef"
        }
    }

    object NoteContent {
        const val Path = "note_content"
        object Fields {
            const val Entry = "entry"
            const val Text = "text"
        }
    }

    object User {
        const val Path = "users"
        object Fields {
            const val Username = "username"
            const val FirstName = "firstName"
            const val LastName = "lastName"
        }
    }

    object ContactRequest {
        const val Path = "contact_requests"
        object Fields {
            const val From = "from"
            const val To = "to"
        }
    }

    object Contact {
        const val Path = "contacts"
        object Fields {
            const val Users = "users"
        }
    }
}
