package io.github.alexeychurchill.stickynotes.fragment.users;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.alexeychurchill.stickynotes.R;

/**
 * Friends fragment
 */

public class FriendsFragment extends Fragment {
    private FriendsListFragment mFriendsListFragment;
    private IncomingFriendRequestListFragment mIncomingFriendRequestListFragment;
    private UserFriendRequestListFragment mUserFriendRequestListFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsListFragment = new FriendsListFragment();
        mIncomingFriendRequestListFragment = new IncomingFriendRequestListFragment();
        mUserFriendRequestListFragment = new UserFriendRequestListFragment();

        ViewPager pager = ((ViewPager) view.findViewById(R.id.pager));
        setupViewPager(pager);

        TabLayout tabs = ((TabLayout) view.findViewById(R.id.tabs));
        tabs.setupWithViewPager(pager);

        return view;
    }

    private void setupViewPager(ViewPager pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addPage(
                mFriendsListFragment,
                getString(R.string.text_title_friends)
        );
        adapter.addPage(
                mIncomingFriendRequestListFragment,
                getString(R.string.text_title_incoming)
        );
        adapter.addPage(
                mUserFriendRequestListFragment,
                getString(R.string.text_title_by_user)
        );
        pager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mPages = new ArrayList<>();
        private List<String> mTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
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
