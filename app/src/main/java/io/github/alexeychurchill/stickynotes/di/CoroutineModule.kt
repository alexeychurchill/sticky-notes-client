package io.github.alexeychurchill.stickynotes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CoroutineModule {

    companion object {
        @Singleton
        @Provides
        fun provideDispatcherProvider(): DispatcherProvider {
            return object : DispatcherProvider {
                override val main: CoroutineDispatcher = Dispatchers.Main.immediate

                override val io: CoroutineDispatcher = Dispatchers.IO

                override val default: CoroutineDispatcher = Dispatchers.Default
            }
        }
    }
}
