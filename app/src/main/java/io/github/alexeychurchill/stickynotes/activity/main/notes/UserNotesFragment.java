package io.github.alexeychurchill.stickynotes.activity.main.notes;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi;

/**
 * User notes fragment
 */

public class UserNotesFragment extends BaseNotesFragment {
    private int mPage = 0;
    private StickyNotesApi mApi;

    @Override
    public void onInit() {
        setFabIcon(R.drawable.ic_add_white_36dp);
        setFabVisible(true);
    }

    @Override
    public void onFabClick() {
    }
}
