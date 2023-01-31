package io.github.alexeychurchill.stickynotes.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactListViewModel
import io.github.alexeychurchill.stickynotes.contacts.ui.ContactListScreen
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme
import io.github.alexeychurchill.stickynotes.dialog.FriendSearchDialogFragment

/**
 * Base friends list fragment
 */
@AndroidEntryPoint
class FriendsListFragment : Fragment() {

    private val viewModel by viewModels<ContactListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val composeView = view as? ComposeView ?: return
        composeView.setContent {
            StickyNotesTheme {
                ContactListScreen(viewModel)
            }
        }
    }

    /**
     * TODO: Add handling of Contact Adding
     */
    private fun addFriends() {
        val dialogFragment = FriendSearchDialogFragment()
        dialogFragment.show(childFragmentManager, "FriendSearchDialogFragment")
    }
}
