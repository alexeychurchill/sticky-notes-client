package io.github.alexeychurchill.stickynotes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.alexeychurchill.stickynotes.account.domain.UserRepository
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class StickyApp : Application(), CoroutineScope {

    @Inject
    lateinit var dispatchers: DispatcherProvider

    @Inject
    lateinit var userRepository: UserRepository

    override val coroutineContext: CoroutineContext by lazy {
        SupervisorJob() + dispatchers.main + CoroutineExceptionHandler { _, _ ->  }
    }

    override fun onCreate() {
        super.onCreate()
        launch {
            userRepository.currentUserSync()
        }
    }
}
