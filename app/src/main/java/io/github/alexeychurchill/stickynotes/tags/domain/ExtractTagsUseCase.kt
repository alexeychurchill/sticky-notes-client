package io.github.alexeychurchill.stickynotes.tags.domain

import javax.inject.Inject

class ExtractTagsUseCase @Inject constructor() {

    private val tagRegex by lazy {
        Regex(pattern = "#([\\w\\d_]+)")
    }

    operator fun invoke(noteText: String): List<String> = tagRegex
        .findAll(noteText)
        .mapNotNull { match -> match.groupValues.getOrNull(1) }
        .toList()
}
