package io.github.alexeychurchill.stickynotes.contacts.ui

import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactScreenMode
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactScreenMode.CONTACTS
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactScreenMode.REQUESTS
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactScreenModeState

val ContactsTabsTexts = mapOf(
    CONTACTS to R.string.screen_contacts_contacts,
    REQUESTS to R.string.screen_contacts_requests,
)

val ContactsTabsIcons = mapOf(
    CONTACTS to Icons.Rounded.Person,
    REQUESTS to Icons.Rounded.CompareArrows,
)

@Composable
fun ContactsTabs(
    modifier: Modifier = Modifier,
    state: ContactScreenModeState,
    onSelectMode: (ContactScreenMode) -> Unit,
) {
    val (tabs, selected) = state
    val selectedIndex = tabs.indexOf(selected).takeIf { it >= 0 } ?: return
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                height = 4.dp,
            )
        },
    ) {
        for (tab in tabs) {
            ContactsTab(
                mode = tab,
                isActive = selected == tab,
                onClick = { onSelectMode(tab) },
            )
        }
    }
}

@Composable
private fun ContactsTab(
    mode: ContactScreenMode,
    isActive: Boolean = false,
    onClick: () -> Unit,
) {
    val iconVector = ContactsTabsIcons[mode]
        ?: throw IllegalArgumentException("There is no icon for $mode")

    val textResId = ContactsTabsTexts[mode]
        ?: throw IllegalArgumentException("There is no text for $mode")

    Tab(
        selected = isActive,
        onClick = onClick,
        icon = { Icon(imageVector = iconVector, contentDescription = null) },
        text = { Text(text = stringResource(textResId).uppercase()) },
    )
}
