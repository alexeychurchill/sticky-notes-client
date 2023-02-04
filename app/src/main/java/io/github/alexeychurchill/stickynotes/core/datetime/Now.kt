package io.github.alexeychurchill.stickynotes.core.datetime

interface Now {
    operator fun invoke(): Long
}
