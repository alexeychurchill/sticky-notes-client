package io.github.alexeychurchill.stickynotes.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.adapter.MenuAdapter
import io.github.alexeychurchill.stickynotes.fragment.notes.NotesFragment
import io.github.alexeychurchill.stickynotes.contacts.FriendsFragment

/**
 * Main application activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnItemClickListener, View.OnClickListener {
    private lateinit var mMenuStrings: Array<String>
    private lateinit var mMenuDrawables: Array<Drawable?>
    private var mCurrentFragment: Fragment? = null
    private var mNotesFragment: NotesFragment? = null
    private var mFriendsFragment: FriendsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        // Init menu
        initMenuData()
        val menuAdapter = MenuAdapter(this, mMenuStrings, mMenuDrawables)
        val lvMenu = findViewById<View>(R.id.lvMenu) as ListView
        lvMenu.adapter = menuAdapter
        lvMenu.onItemClickListener = this
        // Logout button event
        val btnLogout = findViewById<View>(R.id.btnLogout) as Button
        btnLogout?.setOnClickListener(this)
        // Fragments
        mNotesFragment = NotesFragment()
        mFriendsFragment =
            FriendsFragment()
        mCurrentFragment = mNotesFragment
        // Initial fragment
        val manager = supportFragmentManager
        manager.beginTransaction()
            .add(R.id.flContent, mNotesFragment!!)
            .commit()
    }

    private fun initMenuData() {
        mMenuStrings = arrayOf(
            resources.getString(R.string.text_main_menu_notes),
            resources.getString(R.string.text_main_menu_friends)
        )
        mMenuDrawables = arrayOf(
            ContextCompat.getDrawable(this, R.drawable.ic_note_white_36dp),
            ContextCompat.getDrawable(this, R.drawable.ic_people_white_36dp)
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnLogout -> logout()
        }
    }

    override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
        when (i) {
            0 -> showNotesFragment()
            1 -> showFriendsFragment()
        }
    }

    private fun showNotesFragment() {
        showFragment(mNotesFragment, false)
    }

    private fun showFriendsFragment() {
        showFragment(mFriendsFragment, false)
    }

    private fun showFragment(fragment: Fragment?, addToBackStack: Boolean) {
        if (mCurrentFragment === fragment) {
            return
        }
        mCurrentFragment = fragment
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
            .replace(R.id.flContent, fragment!!)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun logout() {
        //...
    }
}