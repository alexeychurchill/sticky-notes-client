package io.github.alexeychurchill.stickynotes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.alexeychurchill.stickynotes.core.datetime.DateTimeFormatter
import io.github.alexeychurchill.stickynotes.core.datetime.RegularDateTimeFormatter
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DateTimeModule {
    @Singleton
    @Binds
    abstract fun bindDateTimeFormatter(impl: RegularDateTimeFormatter): DateTimeFormatter
}
