package com.ds9712.ds_9712.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.databinding.FragmentLauncherBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.intro.IntroActivity
import com.ds9712.ds_9712.ui.main.MainActivity
import com.ds9712.ds_9712.util.ErrorHandling.Companion.NETWORK_ERROR
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LauncherFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    val sharedPreferences: SharedPreferences
): BaseAuthFragment(viewModelFactory) {

    private var _binding: FragmentLauncherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLauncherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        setOnClickListeners()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.accountStatus?.let { accountStatusResponse ->
                println("Launcher Fragment: $accountStatusResponse")
                if (accountStatusResponse.agreementAccepted) {
                    if (accountStatusResponse.emailConfirmed) {
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
                viewModel.resetAuthToken()
                viewModel.resetAccountStatus()
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == -1) {
                    if (stateMessage.response.message == NETWORK_ERROR) {
                        uiCommunicationListener.displaySnackBarMessage(stateMessage.response.message)
                    }
                    viewModel.clearStateMessage()
                }
            }
        })
    }

    private fun setOnClickListeners() {
        binding.launcherFragmentLoginButton.setOnClickListener {
            navLogin()
        }

        binding.launcherFragmentSignUpButton.setOnClickListener {
            navSignUp()
        }
    }

    private fun navLogin() {
//        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    private fun navSignUp() {
//        findNavController().navigate(R.id.action_launcherFragment_to_signUpStepOneFragment)
    }

    private fun navAgreement() {
//        findNavController().navigate(R.id.action_launcherFragment_to_agreementFragment)
    }

    private fun navVerifyEmail() {
//        findNavController().navigate(R.id.action_launcherFragment_to_verifyEmailFragment)
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