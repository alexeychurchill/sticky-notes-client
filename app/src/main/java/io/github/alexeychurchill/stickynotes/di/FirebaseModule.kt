package io.github.alexeychurchill.stickynotes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.alexeychurchill.stickynotes.account.AccountRepository
import io.github.alexeychurchill.stickynotes.account.FirebaseAccountRepository
import io.github.alexeychurchill.stickynotes.notes.firebase.FirebaseNoteRepository
import io.github.alexeychurchill.stickynotes.notes.domain.NoteRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class FirebaseModule {

    @Singleton
    @Binds
    abstract fun bindLoginRepository(impl: FirebaseAccountRepository): AccountRepository

    @Singleton
    @Binds
    abstract fun bindNoteRepository(impl: FirebaseNoteRepository): NoteRepository

    companion object {
        @Provides
        fun provideAuth(): FirebaseAuth = Firebase.auth

        @Provides
        fun provideFirestore(): FirebaseFirestore = Firebase.firestore
    }
}
