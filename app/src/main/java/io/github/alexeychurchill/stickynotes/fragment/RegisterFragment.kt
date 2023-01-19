package io.github.alexeychurchill.stickynotes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.auth.RegisterViewModel

/**
 * Register fragment
 */
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var mETLogin: EditText? = null
    private var mETPassword: EditText? = null
    private var mBtnRegister: Button? = null
    private var mPBWait: ProgressBar? = null

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_register,
        container,
        false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mETLogin = view.findViewById<View>(R.id.etLogin) as EditText
        mETPassword = view.findViewById<View>(R.id.etPassword) as EditText
        mPBWait = view.findViewById<View>(R.id.pbWait) as ProgressBar
        mBtnRegister = view.findViewById<View>(R.id.btnRegister) as Button
        initRegisterButtonListener()
        initProgress()
        initError()
    }

    private fun initRegisterButtonListener() {
        mBtnRegister!!.setOnClickListener {
            val login = mETLogin!!.text.toString()
            val password = mETPassword!!.text.toString()
            viewModel.register(login, password)
        }
    }

    private fun initProgress() {
        lifecycleScope.launchWhenStarted {
            viewModel.isInProgress.collect { isInProgress ->
                mPBWait!!.isInvisible = !isInProgress
                mBtnRegister!!.isInvisible = isInProgress
            }
        }
    }

    private fun initError() {
        lifecycleScope.launchWhenStarted {
            viewModel.error.collect { errorResId ->
                Toast.makeText(requireContext(), errorResId, LENGTH_LONG)
                    .show()
            }
        }
    }
}
