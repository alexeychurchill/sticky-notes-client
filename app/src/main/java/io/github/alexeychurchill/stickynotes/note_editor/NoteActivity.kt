package io.github.alexeychurchill.stickynotes.note_editor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.api.AppConfig
import io.github.alexeychurchill.stickynotes.dialog.EditNoteMetadataDialogFragment
import io.github.alexeychurchill.stickynotes.dialog.ShareNoteDialogFragment
import io.github.alexeychurchill.stickynotes.dialog.SharedToDialogFragment
import io.github.alexeychurchill.stickynotes.model.NoteFull

/**
 * Note view and edit activity
 */
@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {
    private var mNote: NoteFull? = null
    private var mNoteId = -1
    private var mShared = false

    private var mETNoteText: EditText? = null
    private var mPBWait: ProgressBar? = null
    private var mFab: FloatingActionButton? = null

    private val viewModel by viewModels<NoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        // Toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        // Views
        initViews()

        // Initialize with data
        if (!initData(intent)) {
            finish()
            return
        }

        // Observe ViewModel
        lifecycleScope.launchWhenCreated {
            viewModel.inProgress.collect { isInProgress ->
                mETNoteText?.isVisible = !isInProgress
                mPBWait?.isVisible = isInProgress
                mFab?.apply { if (isInProgress) hide() else show() }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.text.collect { text ->
                mETNoteText?.setText(text)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isEditable.collect { isEditable ->
                /** TODO: Set editable field **/
            }
        }

        // TODO: Forward ID to viewModel
        /** viewModel.initialise() **/
    }

    private fun initData(intent: Intent): Boolean {
        // Note id
        if (!intent.hasExtra(EXTRA_NOTE_ID)) {
            Toast.makeText(this, R.string.text_no_note_id, Toast.LENGTH_SHORT)
                .show()
            return false
        }
        mNoteId = intent.getIntExtra(EXTRA_NOTE_ID, mNoteId)
        // Is shared
        if (!intent.hasExtra(EXTRA_NOTE_SHARED)) {
            Toast.makeText(this, R.string.text_no_shared_flag, Toast.LENGTH_SHORT)
                .show()
            return false
        }
        mShared = intent.getBooleanExtra(EXTRA_NOTE_SHARED, mShared)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (mShared) {
            menuSwitchShared(menu)
        } else {
            menuSwitchOwn(menu)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuShareNote) {
            shareThisNote()
            return true
        }
        if (item.itemId == R.id.menuSharedTo) {
            sharedTo()
            return true
        }
        if (item.itemId == R.id.menuEditMetadata) {
            editMetadata()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun editMetadata() {
        val dialog = EditNoteMetadataDialogFragment()
        dialog.setNote(mNote)
        dialog.show(supportFragmentManager, "EditNoteMetadataDialogFragment")
    }

    private fun sharedTo() {
        val dialog = SharedToDialogFragment()
        dialog.setNoteId(mNoteId)
        dialog.show(supportFragmentManager, "SharedToDialogFragment")
    }

    private fun shareThisNote() {
        val dialog = ShareNoteDialogFragment()
        dialog.setNoteId(mNoteId)
        dialog.show(supportFragmentManager, "ShareNoteDialogFragment")
    }

    private fun menuSwitchShared(menu: Menu) {
        for (menuNumber in 0 until menu.size()) {
            val menuItem = menu.getItem(menuNumber)
            when (menuItem.itemId) {
                R.id.menuSharedTo, R.id.menuShareNote, R.id.menuEditMetadata -> menuItem.isVisible =
                    false
            }
        }
    }

    private fun menuSwitchOwn(menu: Menu) {
        for (menuNumber in 0 until menu.size()) {
            val menuItem = menu.getItem(menuNumber)
            when (menuItem.itemId) {
                R.id.menuEditMetadata, R.id.menuSharedTo, R.id.menuShareNote -> menuItem.isVisible =
                    true
            }
        }
    }

    private fun initViews() {
        mETNoteText = findViewById<View>(R.id.etNoteText) as EditText
        mPBWait = findViewById<View>(R.id.pbWait) as ProgressBar
        mFab = findViewById<View>(R.id.fab) as FloatingActionButton
        mFab!!.setOnClickListener {
            viewModel.saveNote()
        }
    }

    companion object {
        const val EXTRA_NOTE_ID = AppConfig.APP_PACKAGE + ".EXTRA_NOTE_ID"
        const val EXTRA_NOTE_SHARED = AppConfig.APP_PACKAGE + ".EXTRA_NOTE_SHARED"
    }
}
