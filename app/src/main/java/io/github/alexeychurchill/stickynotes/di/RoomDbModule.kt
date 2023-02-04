package io.github.alexeychurchill.stickynotes.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.alexeychurchill.stickynotes.core.data.Database
import io.github.alexeychurchill.stickynotes.note_editor.data.RoomNoteRepository
import io.github.alexeychurchill.stickynotes.note_editor.domain.NoteRepository
import io.github.alexeychurchill.stickynotes.notes.data.RoomNoteEntryRepository
import io.github.alexeychurchill.stickynotes.notes.domain.NoteEntryRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RoomDbModule {

    @Singleton
    @Binds
    abstract fun bindNoteEntryRepository(impl: RoomNoteEntryRepository): NoteEntryRepository

    @Singleton
    @Binds
    abstract fun bindNoteRepository(impl: RoomNoteRepository): NoteRepository

    companion object {

        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context,
        ): Database = Room
            .databaseBuilder(
                context,
                Database::class.java,
                "sn-database"
            )
            .build()
    }
}
