package io.github.alexeychurchill.stickynotes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.alexeychurchill.stickynotes.core.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class StickyApp : Application(), CoroutineScope {

    @Inject
    lateinit var dispatchers: DispatcherProvider

    override val coroutineContext: CoroutineContext by lazy {
        SupervisorJob() + dispatchers.main + CoroutineExceptionHandler { _, _ ->  }
    }
}
