package io.github.alexeychurchill.stickynotes.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.activity.MainActivity
import io.github.alexeychurchill.stickynotes.api.AppConfig
import io.github.alexeychurchill.stickynotes.listener.OnLoggedInListener

/**
 * Login and register activity
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener, OnLoggedInListener {
    private var mTVButtonActionTitle: TextView? = null
    private var mAction = Action.LOGIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val preferences = getSharedPreferences(AppConfig.APP_PREFERENCES, MODE_PRIVATE)
        preferences.edit()
            .putString(AppConfig.SHARED_BASE_URL, AppConfig.BASE_URL) // TODO: 26.12.2016 Delete
            .apply()

        // Initial fragment
        val manager = supportFragmentManager
        manager.beginTransaction()
            .add(R.id.flFragmentContainer, LoginFragment())
            .commit()

        // Swap fragments button
        mTVButtonActionTitle = findViewById<View>(R.id.tvButtonActionTitle) as TextView
        val llButtonSwapAction = findViewById<View>(R.id.llButtonSwapAction) as LinearLayout
        llButtonSwapAction.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.llButtonSwapAction -> swapAction()
        }
    }

    private fun swapAction() {
        when (mAction) {
            Action.LOGIN -> switchActionRegister()
            Action.REGISTER -> switchActionLogin()
        }
    }

    private fun switchActionLogin() {
        mAction = Action.LOGIN
        switchFragment(LoginFragment())
        mTVButtonActionTitle!!.setText(R.string.text_button_join)
    }

    private fun switchActionRegister() {
        mAction = Action.REGISTER
        switchFragment(RegisterFragment())
        mTVButtonActionTitle!!.setText(R.string.text_button_back_login)
    }

    private fun switchFragment(fragment: Fragment?) {
        val manager = supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.flFragmentContainer, fragment!!)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onLoggedId(accessToken: String) {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private enum class Action {
        LOGIN, REGISTER
    }
}