package io.github.alexeychurchill.stickynotes.note_editor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.api.AppConfig
import io.github.alexeychurchill.stickynotes.api.StickyNotesApi
import io.github.alexeychurchill.stickynotes.api.callback.SimpleResponseCallback
import io.github.alexeychurchill.stickynotes.dialog.EditNoteMetadataDialogFragment
import io.github.alexeychurchill.stickynotes.dialog.ShareNoteDialogFragment
import io.github.alexeychurchill.stickynotes.dialog.SharedToDialogFragment
import io.github.alexeychurchill.stickynotes.model.NoteFull
import io.github.alexeychurchill.stickynotes.model.ServiceResponse
import io.github.alexeychurchill.stickynotes.model.SharedNoteFull
import io.github.alexeychurchill.stickynotes.model.deserializer.NoteFullResponseDeserializer
import io.github.alexeychurchill.stickynotes.model.deserializer.SharedNoteResponseDeserializer
import io.github.alexeychurchill.stickynotes.model.deserializer.SimpleResponseDeserializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Note view and edit activity
 */
class NoteActivity : AppCompatActivity(), View.OnClickListener {
    private var mApi: StickyNotesApi? = null
    private var mHasNote = false
    private var mNote: NoteFull? = null
    private var mNoteId = -1
    private var mEditable = true
    private var mShared = false
    private var mAccessToken: String? = null
    private var mETNoteText: EditText? = null
    private var mPBWait: ProgressBar? = null
    private var mFab: FloatingActionButton? = null
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
        // API
        mSimpleResponseCallback = SimpleResponseCallback(this)
        // Base URL
        val baseUrl = getSharedPreferences(AppConfig.APP_PREFERENCES, MODE_PRIVATE)
            .getString(AppConfig.SHARED_BASE_URL, null)
        if (baseUrl == null) {
            Toast.makeText(this, R.string.text_no_base_url, Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }
        // Access token
        mAccessToken = getSharedPreferences(AppConfig.APP_PREFERENCES, MODE_PRIVATE)
            .getString(AppConfig.SHARED_ACCESS_TOKEN, null)
        if (mAccessToken == null) {
            Toast.makeText(this, R.string.text_access_token_is_null, Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }
        val gson = GsonBuilder()
            .registerTypeAdapter(
                SharedNoteResponseDeserializer.TYPE,
                SharedNoteResponseDeserializer()
            )
            .registerTypeAdapter(NoteFullResponseDeserializer.TYPE, NoteFullResponseDeserializer())
            .registerTypeAdapter(SimpleResponseDeserializer.TYPE, SimpleResponseDeserializer())
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        mApi = retrofit.create(StickyNotesApi::class.java)
        callLoadNote()
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

    override fun onClick(view: View) {
        if (view.id == R.id.fab) {
            callUpdate()
        }
    }

    private fun initViews() {
        mETNoteText = findViewById<View>(R.id.etNoteText) as EditText
        mPBWait = findViewById<View>(R.id.pbWait) as ProgressBar
        mFab = findViewById<View>(R.id.fab) as FloatingActionButton
        mFab!!.setOnClickListener(this)
    }

    private fun setNote(note: NoteFull) {
        mNote = note
        mEditable = true
        mHasNote = true
        mETNoteText!!.setText(note.text)
        mETNoteText!!.isClickable = true
        mETNoteText!!.isFocusable = true
        mFab!!.show()
    }

    private fun setNote(note: SharedNoteFull) {
        mNote = note.note
        mEditable = note.canEdit()
        mHasNote = true
        mETNoteText!!.setText(mNote!!.text)
        mETNoteText!!.isClickable = mEditable
        mETNoteText!!.isFocusable = mEditable
        if (mEditable) {
            mFab!!.show()
        } else {
            mFab!!.hide()
        }
    }

    private fun setWaiting(waiting: Boolean) {
        if (waiting) {
            mFab!!.hide()
        } else {
            mFab!!.show()
        }
        mETNoteText!!.visibility = if (waiting) View.INVISIBLE else View.VISIBLE
        mPBWait!!.visibility = if (waiting) View.VISIBLE else View.INVISIBLE
    }

    private fun callLoadNote() {
        if (mNoteId == -1 || mAccessToken == null) {
            return
        }
        setWaiting(true)
        if (mShared) {
            val call = mApi!!.sharedGet(mAccessToken, mNoteId)
            call.enqueue(mSharedNoteCallback)
        } else { // users note call
            val call = mApi!!.noteGet(mAccessToken, mNoteId)
            call.enqueue(mNoteCallback)
        }
    }

    private fun callUpdate() {
        if (!mHasNote || mAccessToken == null) {
            return
        }
        if (mShared) {
            callSharedNoteUpdate()
        } else {
            callUserNoteUpdate()
        }
    }

    private fun callSharedNoteUpdate() {
        if (!mEditable) {
            return
        }
        val newText = mETNoteText!!.text.toString()
        val call = mApi!!.sharedUpdate(mAccessToken, mNote!!.id, newText)
        call.enqueue(mSimpleResponseCallback)
    }

    private fun callUserNoteUpdate() {
        val newText = mETNoteText!!.text.toString()
        val call = mApi!!.noteUpdate(mAccessToken, mNote!!.id, newText)
        call.enqueue(mSimpleResponseCallback)
    }

    private var mSimpleResponseCallback: SimpleResponseCallback? = null
    private val mNoteCallback: Callback<ServiceResponse<NoteFull>> =
        object : Callback<ServiceResponse<NoteFull>> {
            override fun onResponse(
                call: Call<ServiceResponse<NoteFull>>,
                response: Response<ServiceResponse<NoteFull>>
            ) {
                setWaiting(false)
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@NoteActivity, response.message()
                                + " (" + response.code().toString() + ")",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val noteFullResponse = response.body()
                if (noteFullResponse.isError && !noteFullResponse.containsData()) {
                    Toast.makeText(this@NoteActivity, noteFullResponse.message, Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val note = noteFullResponse.data
                setNote(note)
            }

            override fun onFailure(call: Call<ServiceResponse<NoteFull>>, t: Throwable) {
                Toast.makeText(this@NoteActivity, R.string.text_failure, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    private val mSharedNoteCallback: Callback<ServiceResponse<SharedNoteFull>> =
        object : Callback<ServiceResponse<SharedNoteFull>> {
            override fun onResponse(
                call: Call<ServiceResponse<SharedNoteFull>>,
                response: Response<ServiceResponse<SharedNoteFull>>
            ) {
                setWaiting(false)
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@NoteActivity, response.message()
                                + " (" + response.code().toString() + ")",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val sharedNoteResponse = response.body()
                if (sharedNoteResponse.isError && !sharedNoteResponse.containsData()) {
                    Toast.makeText(
                        this@NoteActivity,
                        sharedNoteResponse.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return
                }
                val sharedNoteFull = sharedNoteResponse.data
                setNote(sharedNoteFull)
            }

            override fun onFailure(call: Call<ServiceResponse<SharedNoteFull>>, t: Throwable) {
                Toast.makeText(this@NoteActivity, R.string.text_failure, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    companion object {
        const val EXTRA_NOTE_ID = AppConfig.APP_PACKAGE + ".EXTRA_NOTE_ID"
        const val EXTRA_NOTE_SHARED = AppConfig.APP_PACKAGE + ".EXTRA_NOTE_SHARED"
    }
}