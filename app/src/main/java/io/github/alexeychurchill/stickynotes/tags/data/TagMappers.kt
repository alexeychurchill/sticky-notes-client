package io.github.alexeychurchill.stickynotes.tags.data

import io.github.alexeychurchill.stickynotes.tags.domain.Tag

fun RoomTag.toDomain(): Tag = Tag(
    name = name,
)

fun RoomTagUsage.toDomain(): Tag = Tag(
    name = name,
)
