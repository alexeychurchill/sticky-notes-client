package io.github.alexeychurchill.stickynotes.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.alexeychurchill.stickynotes.core.datetime.DateTimeFormatter
import io.github.alexeychurchill.stickynotes.core.datetime.Now
import io.github.alexeychurchill.stickynotes.core.datetime.RegularDateTimeFormatter
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DateTimeModule {

    @Singleton
    @Binds
    abstract fun bindDateTimeFormatter(impl: RegularDateTimeFormatter): DateTimeFormatter

    companion object {

        @Singleton
        @Provides
        fun provideNow(): Now = object : Now {
            override fun invoke(): Long = Calendar
                .getInstance()
                .timeInMillis
        }
    }
}
