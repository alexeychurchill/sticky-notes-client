package io.github.alexeychurchill.stickynotes.app

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.alexeychurchill.stickynotes.note_editor.ui.NoteScreen
import io.github.alexeychurchill.stickynotes.notes.ui.UserNotesList

fun NavGraphBuilder.rootNavGraph(navController: NavHostController) {

    composable(Route.NoteList.routePath) {
        UserNotesList(
            navController = navController,
            viewModel = hiltViewModel(),
        )
    }

    composable(
        route = Route.NoteEditor.PathTemplate,
        arguments = listOf(
            navArgument(Route.NoteEditor.ArgNoteId) { type = NavType.StringType },
        ),
    ) {
        NoteScreen(
            viewModel = hiltViewModel(),
        )
    }
}
