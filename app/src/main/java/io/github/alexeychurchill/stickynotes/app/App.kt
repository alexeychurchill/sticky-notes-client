package io.github.alexeychurchill.stickynotes.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme

@Composable
fun StickyNotesApp() {
    val navController = rememberNavController()
    StickyNotesTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { })
            },
        ) { paddings ->
            NavHost(
                modifier = Modifier.padding(paddings),
                navController = navController,
                startDestination = Route.Start.routePath,
                builder = { rootNavGraph(navController) },
            )
        }
    }
}
