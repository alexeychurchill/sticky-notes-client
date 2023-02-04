package io.github.alexeychurchill.stickynotes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import io.github.alexeychurchill.stickynotes.notes.firebase.MockNoteEntryRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class MockDataModule {

    @Singleton
    @Binds
    abstract fun bindNoteEntryRepository(impl: MockNoteEntryRepository): NoteEntryRepository
}
