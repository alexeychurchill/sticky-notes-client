@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.alexeychurchill.stickynotes.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme

@Composable
fun StickyNotesApp() {
    val navController = rememberNavController()
    StickyNotesTheme {
        Scaffold { paddings ->
            NavHost(
                modifier = Modifier.padding(paddings),
                navController = navController,
                startDestination = Route.Start.routePath,
                builder = { rootNavGraph(navController) },
            )
        }
    }
}
