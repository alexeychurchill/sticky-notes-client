package io.github.alexeychurchill.stickynotes.tags.data

import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import io.github.alexeychurchill.stickynotes.core.data.Database
import io.github.alexeychurchill.stickynotes.tags.domain.Tag
import io.github.alexeychurchill.stickynotes.tags.domain.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomTagRepository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val db: Database,
) : TagRepository {

    override val allTags: Flow<List<Tag>>
        get() = db.tagDao()
            .flowUniqueSortByUsage()
            .map { tags -> tags.map(RoomTagUsage::toDomain) }
            .flowOn(dispatchers.io)

    override val allTagsNotes: Flow<List<Pair<Tag, Long>>>
        get() = db.tagDao()
            .flowAll()
            .map { tags ->
                tags.map { tagEntity -> tagEntity.toDomain() to tagEntity.noteId }
            }
            .flowOn(dispatchers.io)

    override suspend fun saveTags(noteId: Long, tags: List<String>) = withContext(dispatchers.io) {
        val lowercaseTags = tags.map(String::lowercase)
        val tagEntities = lowercaseTags.map { tag ->
            RoomTag(noteId = noteId, name = tag)
        }
        db.runInTransaction {
            db.tagDao().run {
                deleteAllByNoteId(noteId)
                insertAll(tagEntities)
            }
        }
    }
}
