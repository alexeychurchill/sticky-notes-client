package io.github.alexeychurchill.stickynotes.fragment.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.fragment.notes.SharedNotesFragment;
import io.github.alexeychurchill.stickynotes.fragment.notes.UserNotesFragment;

/**
 * Notes fragment
 */

public class NotesFragment extends Fragment {
    private UserNotesFragment mUserNotesFragment;
    private SharedNotesFragment mSharedNotesFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragments
        mUserNotesFragment = new UserNotesFragment();
        mSharedNotesFragment = new SharedNotesFragment();
        // View
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        // Tabs
        ViewPager pager = ((ViewPager) view.findViewById(R.id.pager));
        setupViewPager(pager);
        TabLayout tabs = ((TabLayout) view.findViewById(R.id.tabs));
        tabs.setupWithViewPager(pager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        getChildFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addPage(
                mUserNotesFragment,
                getContext().getString(R.string.text_title_page_user_notes)
        );
        adapter.addPage(
                mSharedNotesFragment,
                getContext().getString(R.string.text_title_page_shared_notes)
        );
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mPages = new ArrayList<>();
        private List<String> mTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mPages.get(position);
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        public void addPage(Fragment fragment, String title) {
            mPages.add(fragment);
            mTitles.add(title);
        }
    }
}
