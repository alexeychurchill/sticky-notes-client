package io.github.alexeychurchill.stickynotes.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.R

/**
 * Login fragment
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var mETLogin: EditText? = null
    private var mETPassword: EditText? = null
    private var mPBWait: ProgressBar? = null
    private var mBtnLogin: Button? = null

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_login,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mETLogin = view.findViewById<View>(R.id.etLogin) as EditText
        mETPassword = view.findViewById<View>(R.id.etPassword) as EditText
        mPBWait = view.findViewById<View>(R.id.pbWait) as ProgressBar
        mBtnLogin = view.findViewById<View>(R.id.btnLogin) as Button

        initLoginButtonListener()
        initErrors()
        initProgress()
    }

    private fun initLoginButtonListener() {
        mBtnLogin!!.setOnClickListener {
            val login = mETLogin!!.text.toString()
            val password = mETPassword!!.text.toString()
            viewModel.login(login, password)
        }
    }

    private fun initErrors() {
        lifecycleScope.launchWhenStarted {
            viewModel.error.collect { errorResId ->
                Toast
                    .makeText(context, errorResId, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initProgress() {
        lifecycleScope.launchWhenStarted {
            viewModel.isInProgress.collect { isInProgress ->
                mPBWait!!.isVisible = isInProgress
                mBtnLogin!!.isInvisible = isInProgress
            }
        }
    }
}
