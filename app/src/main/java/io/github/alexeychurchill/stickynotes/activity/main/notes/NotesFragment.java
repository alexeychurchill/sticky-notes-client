package io.github.alexeychurchill.stickynotes.activity.main.notes;

import android.content.SharedPreferences;
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(
                mUserNotesFragment,
                getContext().getString(R.string.text_title_page_user_notes)
        );
        adapter.addFragment(
                mSharedNotesFragment,
                getContext().getString(R.string.text_title_page_shared_notes)
        );
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }
    }
}
