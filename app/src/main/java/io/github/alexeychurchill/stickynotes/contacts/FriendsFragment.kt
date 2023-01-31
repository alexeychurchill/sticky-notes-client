package io.github.alexeychurchill.stickynotes.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.R

/**
 * Friends fragment
 */
@AndroidEntryPoint
class FriendsFragment : Fragment() {
    private var mFriendsListFragment: FriendsListFragment? = null
    private var mIncomingFriendRequestListFragment: IncomingFriendRequestListFragment? = null
    private var mUserFriendRequestListFragment: UserFriendRequestListFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)
        mFriendsListFragment = FriendsListFragment()
        mIncomingFriendRequestListFragment = IncomingFriendRequestListFragment()
        mUserFriendRequestListFragment = UserFriendRequestListFragment()
        val pager = view.findViewById<View>(R.id.pager) as ViewPager
        setupViewPager(pager)
        val tabs = view.findViewById<View>(R.id.tabs) as TabLayout
        tabs.setupWithViewPager(pager)
        return view
    }

    private fun setupViewPager(pager: ViewPager) {
        val adapter: ViewPagerAdapter = ViewPagerAdapter(
            childFragmentManager
        )
        adapter.addPage(
            mFriendsListFragment,
            getString(R.string.text_title_friends)
        )
        adapter.addPage(
            mIncomingFriendRequestListFragment,
            getString(R.string.text_title_incoming)
        )
        adapter.addPage(
            mUserFriendRequestListFragment,
            getString(R.string.text_title_by_user)
        )
        pager.adapter = adapter
    }

    private inner class ViewPagerAdapter(fragmentManager: FragmentManager?) : FragmentPagerAdapter(
        fragmentManager!!
    ) {
        private val mPages: MutableList<Fragment> = ArrayList()
        private val mTitles: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mPages[position]
        }

        override fun getCount(): Int {
            return mPages.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles[position]
        }

        fun addPage(fragment: Fragment?, title: String) {
            mPages.add(fragment!!)
            mTitles.add(title)
        }
    }
}