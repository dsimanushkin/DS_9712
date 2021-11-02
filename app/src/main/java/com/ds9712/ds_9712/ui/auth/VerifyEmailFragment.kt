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
import com.ds9712.ds_9712.databinding.FragmentVerifyEmailBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.state.AuthStateEvent
import com.ds9712.ds_9712.ui.intro.IntroActivity
import com.ds9712.ds_9712.ui.main.MainActivity
import com.ds9712.ds_9712.util.ErrorHandling
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class VerifyEmailFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
): BaseAuthFragment(viewModelFactory) {

    private var _binding: FragmentVerifyEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        setOnClickListeners()
        initPinEntryEditText()
    }

    private fun subscribeObservers() {
        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer {stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == 8) {
                    when (stateMessage.response.statusCode) {
                        2021 -> {
                            setUserLoggedIn()
                            if (sharedPreferences.getBoolean("showIntro", true)) {
                                navIntroActivity()
                            } else {
                                navMainActivity()
                            }
                        }
                        2008 -> {
                            // Verification code has been sent
                            uiCommunicationListener.displaySnackBarMessage(ErrorHandling.handleErrors(stateMessage.response.statusCode, requireActivity().application))
                        }
                        -2 -> {
                            displayErrorMessage(stateMessage.response.message.toString())
                        }
                        else -> {
                            displayErrorMessage(ErrorHandling.handleErrors(stateMessage.response.statusCode, requireActivity().application))
                        }
                    }
                    viewModel.clearStateMessage()
                } else {
                    uiCommunicationListener.displaySnackBarMessage(getString(R.string.unable_to_complete_this_request))
                    viewModel.clearStateMessage()
                }
            }
        })
    }

    private fun setOnClickListeners() {
        binding.verifyEmailPageLogoutButton.setOnClickListener {
            viewModel.logout()
            navLauncher()
        }

        binding.verifyEmailPageResendCodeButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                resendVerificationCode()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
            uiCommunicationListener.hideSoftKeyboard()
        }

        binding.verifyEmailPageChangeEmailButton.setOnClickListener {
            navChangeEmailAddress()
        }
    }

    private fun initPinEntryEditText() {
        binding.verifyEmailVerificationCodeEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.verifyEmailPageErrorMessage.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length == 6) {
                    verifyEmail(p0.toString())
                }
            }
        })
    }

    private fun resendVerificationCode() {
        viewModel.setStateEvent(AuthStateEvent.ResendVerificationCodeEvent)
    }

    private fun verifyEmail(verificationCode: String) {
        viewModel.setStateEvent(
            AuthStateEvent.VerifyEmailEvent(
            verificationCode = verificationCode
        ))
    }

    private fun setUserLoggedIn() {
        sharedPrefsEditor.putBoolean("isUserLoggedIn", true)
        sharedPrefsEditor.apply()
    }

    private fun displayErrorMessage(errorMessage: String) {
        binding.verifyEmailPageErrorMessage.text = errorMessage
        binding.verifyEmailPageErrorMessage.visibility = View.VISIBLE
    }

    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.verifyEmailPageLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.verifyEmailPageLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun navChangeEmailAddress() {
        findNavController().navigate(R.id.action_verifyEmailFragment_to_changeEmailAddressFragment)
    }

    private fun navLauncher() {
        findNavController().navigate(R.id.action_verifyEmailFragment_to_launcherFragment)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}