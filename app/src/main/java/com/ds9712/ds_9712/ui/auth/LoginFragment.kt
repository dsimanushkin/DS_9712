package com.ds9712.ds_9712.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.FragmentLogInBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.state.AuthStateEvent
import com.ds9712.ds_9712.ui.auth.state.LoginFields
import com.ds9712.ds_9712.ui.intro.IntroActivity
import com.ds9712.ds_9712.ui.main.MainActivity
import com.ds9712.ds_9712.util.ErrorHandling
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LoginFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
): BaseAuthFragment(viewModelFactory) {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        setOnClickListeners()
        setOnTextChangedListeners()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.loginFields?.let { loginFields ->
                loginFields.loginUsername?.let {
                    binding.logInPageEditTextUsername.setText(it)
                }
                loginFields.loginPassword?.let {
                    binding.logInPageEditTextPassword.setText(it)
                }
            }
            viewState.accountStatus?.let { accountStatusResponse ->
                if (accountStatusResponse.agreementAccepted) {
                    if (accountStatusResponse.emailConfirmed) {
                        setUserLoggedIn()
                        if (sharedPreferences.getBoolean("showIntro", true)) {
                            navIntroActivity()
                        } else {
                            navMainActivity()
                        }
                    } else {
                        navVerifyEmail()
                    }
                } else {
                    navAgreement()
                }
                clearLoginFields()
                viewState.authToken = null
                viewState.accountStatus = null
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer {stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == 1) {
                    if (stateMessage.response.statusCode != 2012) {
                        if (stateMessage.response.statusCode == -2) {
                            setErrorMessage(stateMessage.response.message.toString())
                        } else {
                            setErrorMessage(ErrorHandling.handleErrors(stateMessage.response.statusCode, requireActivity().application))
                        }
                    }
                    viewModel.clearStateMessage()
                } else if (stateMessage.response.requestCode == -1) {
                    uiCommunicationListener.displaySnackBarMessage(getString(R.string.unable_to_complete_this_request))
                    viewModel.clearStateMessage()
                }
            }
        })
    }

    private fun setOnClickListeners() {
        binding.logInPageBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.logInPageSignUpButton.setOnClickListener {
            navSignUp()
        }

        binding.logInPageLoginButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                if (binding.logInPageEditTextUsername.text.isNullOrEmpty()) {
                    binding.logInPageEditTextUsername.requestFocus()
                } else if (binding.logInPageEditTextPassword.text.isNullOrEmpty()) {
                    binding.logInPageEditTextPassword.requestFocus()
                }
                login()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
        }
    }

    private fun setOnTextChangedListeners() {
        binding.logInPageEditTextUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.logInPageEditTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun login() {
        viewModel.setStateEvent(
            AuthStateEvent.LoginEvent(
                binding.logInPageEditTextUsername.text.toString(),
                binding.logInPageEditTextPassword.text.toString()
            ))
    }

    private fun setUserLoggedIn() {
        sharedPrefsEditor.putBoolean("isUserLoggedIn", true)
        sharedPrefsEditor.apply()
    }

    private fun setErrorMessage(errorMessage: String) {
        binding.logInPageTextInputLayoutPassword.helperText = errorMessage
    }

    private fun clearErrorMessage() {
        binding.logInPageTextInputLayoutPassword.helperText = null
    }

    private fun navSignUp() {
        findNavController().navigate(R.id.action_loginFragment_to_signUpStepOneFragment)
    }

    private fun navAgreement() {
        findNavController().navigate(R.id.action_logInFragment_to_agreementFragment)
    }

    private fun navVerifyEmail() {
        findNavController().navigate(R.id.action_logInFragment_to_verifyEmailFragment)
    }

    private fun navIntroActivity() {
        activity?.let {
            val intent = Intent(it, IntroActivity::class.java)
            startActivity(intent)
            it.finish()
            (it.application as BaseApplication).releaseAuthComponent()
        }
    }

    private fun navMainActivity() {
        activity?.let {
            val intent = Intent(it, MainActivity::class.java)
            startActivity(intent)
            it.finish()
            (it.application as BaseApplication).releaseAuthComponent()
        }
    }

    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.logInPageLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.logInPageLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun saveLoginFields() {
        viewModel.setLoginFields(
            LoginFields(
                binding.logInPageEditTextUsername.text.toString(),
                binding.logInPageEditTextPassword.text.toString()
            )
        )
    }

    private fun clearLoginFields() {
        binding.logInPageEditTextUsername.setText("")
        binding.logInPageEditTextPassword.setText("")
        viewModel.setLoginFields(
            LoginFields(
                "",
                ""
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        saveLoginFields()
        _binding = null
    }
}