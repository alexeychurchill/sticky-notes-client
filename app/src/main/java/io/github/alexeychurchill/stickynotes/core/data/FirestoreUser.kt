package io.github.alexeychurchill.stickynotes.core.data

import io.github.alexeychurchill.stickynotes.core.data.FirestoreScheme.User.Fields
import io.github.alexeychurchill.stickynotes.core.model.User

object FirestoreUser {
    fun toFirestore(user: User): Map<String, Any?> = with(user) {
        mapOf(
            Fields.Username to login,
            Fields.FirstName to firstName,
            Fields.LastName to lastName,
        )
    }

    fun toDomain(id: String, fields: Map<String, Any?>): User = User(
        id = id,
        login = fields[Fields.Username] as String,
        firstName = fields[Fields.FirstName] as String,
        lastName = fields[Fields.LastName] as String,
    )
}
