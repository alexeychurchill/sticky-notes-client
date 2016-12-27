package io.github.alexeychurchill.stickynotes.activity.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.activity.main.friends.FriendsListFragment;
import io.github.alexeychurchill.stickynotes.activity.main.notes.NotesFragment;
import io.github.alexeychurchill.stickynotes.activity.main.notes.UserNotesFragment;

/**
 * Main application activity
 */

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener,
        View.OnClickListener {

    private String[] mMenuStrings;
    private Drawable[] mMenuDrawables;

    private Fragment mCurrentFragment;
    private NotesFragment mNotesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar
        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Init menu
        initMenuData();
        MenuAdapter menuAdapter = new MenuAdapter(this, mMenuStrings, mMenuDrawables);
        ListView lvMenu = ((ListView) findViewById(R.id.lvMenu));
        lvMenu.setAdapter(menuAdapter);
        lvMenu.setOnItemClickListener(this);
        // Logout button event
        Button btnLogout = ((Button) findViewById(R.id.btnLogout));
        if (btnLogout != null) {
            btnLogout.setOnClickListener(this);
        }
        // Fragments
        mNotesFragment = new NotesFragment();
        mCurrentFragment = mNotesFragment;
        // Initial fragment
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.flContent, mNotesFragment)
                .commit();
    }

    private void initMenuData() {
        mMenuStrings = new String[] {
                getResources().getString(R.string.text_menu_notes),
                getResources().getString(R.string.text_menu_friends)
        };
        mMenuDrawables = new Drawable[] {
                ContextCompat.getDrawable(this, R.drawable.ic_note_white_36dp),
                ContextCompat.getDrawable(this, R.drawable.ic_people_white_36dp)
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogout:
                logout();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                showNotesFragment();
                break;
            case 1:
                showFriendsFragment();
                break;
        }
    }

    private void showNotesFragment() {
        showFragment(mNotesFragment, false);
    }

    private void showFriendsFragment() {
//        showFragment(new FriendsListFragment(), false);
    }

    private void showFragment(Fragment fragment, boolean addToBackStack) {
        if (mCurrentFragment == fragment) {
            return;
        }
        mCurrentFragment = fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void logout() {
        //...
    }
}
