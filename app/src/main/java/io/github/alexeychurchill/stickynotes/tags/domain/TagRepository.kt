package io.github.alexeychurchill.stickynotes.tags.domain

import kotlinx.coroutines.flow.Flow

interface TagRepository {

    val allTags: Flow<List<Tag>>

    val allTagsNotes: Flow<List<Pair<Tag, Long>>>

    suspend fun saveTags(noteId: Long, tags: List<String>)
}
