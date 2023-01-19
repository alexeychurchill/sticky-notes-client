package io.github.alexeychurchill.stickynotes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.alexeychurchill.stickynotes.auth.FirebaseLoginRepository
import io.github.alexeychurchill.stickynotes.auth.LoginRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class FirebaseModule {

    @Singleton
    @Binds
    abstract fun bindLoginRepository(impl: FirebaseLoginRepository): LoginRepository

    companion object {
        @Provides
        fun provideAuth(): FirebaseAuth = Firebase.auth
    }
}
