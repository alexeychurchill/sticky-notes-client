@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.alexeychurchill.stickynotes.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme

@Composable
fun StickyNotesApp() {
    val navController = rememberNavController()
    StickyNotesTheme {
        Scaffold { paddings ->
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navController,
                    startDestination = Route.Start.routePath,
                    builder = { rootNavGraph(navController) },
                )

                val bottomNavScrimColor = MaterialTheme.colorScheme.background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    bottomNavScrimColor.copy(alpha = 0.0f),
                                    bottomNavScrimColor.copy(alpha = 0.75f),
                                    bottomNavScrimColor,
                                ),
                            ),
                        )
                        .height(paddings.calculateBottomPadding())
                        .align(BottomStart)
                )
            }
        }
    }
}
