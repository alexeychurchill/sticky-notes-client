package io.github.alexeychurchill.stickynotes.app

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.alexeychurchill.stickynotes.note_editor.NoteKeys
import io.github.alexeychurchill.stickynotes.note_editor.ui.NoteScreen
import io.github.alexeychurchill.stickynotes.notes.ui.UserNotesListScreen

fun NavGraphBuilder.rootNavGraph(navController: NavHostController) {

    composable(Route.NoteList.routePath) {
        UserNotesListScreen(
            navController = navController,
            viewModel = hiltViewModel(),
        )
    }

    composable(
        route = Route.NoteEditor.PathTemplate,
        arguments = listOf(
            navArgument(NoteKeys.NoteId) { type = NavType.StringType },
        ),
    ) {
        NoteScreen(
            navController = navController,
            viewModel = hiltViewModel(),
        )
    }
}
