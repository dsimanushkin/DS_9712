package com.ds9712.ds_9712.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.FragmentAgreementBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.state.*
import com.ds9712.ds_9712.util.ErrorHandling
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class AgreementFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
): BaseAuthFragment(viewModelFactory) {

    private var _binding: FragmentAgreementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgreementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        resetAllFields()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == 7) {
                    if (stateMessage.response.statusCode == 2003 ||
                        stateMessage.response.statusCode == 2004) {
                        navVerifyEmail()
                    } else if (stateMessage.response.statusCode == 2006) {
                        viewModel.logout()
                        viewModel.resetAuthToken()
                        viewModel.resetAccountStatus()
                        resetAllFields()
                        navLauncher()
                    } else if (stateMessage.response.statusCode == -2) {
                        uiCommunicationListener.displaySnackBarMessage(stateMessage.response.message.toString())
                    } else {
                        uiCommunicationListener.displaySnackBarMessage(ErrorHandling.handleErrors(stateMessage.response.statusCode, requireActivity().application))
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
        binding.agreementPageLogoutButton.setOnClickListener {
            viewModel.logout()
            viewModel.resetAuthToken()
            viewModel.resetAccountStatus()
            resetAllFields()
            navLauncher()
        }
        binding.agreementPageDeclineButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                declineAgreement()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
        }
        binding.agreementPrivacyPolicyButton.setOnClickListener {
            navPrivacyPolicy()
        }
        binding.agreementPageAcceptButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                acceptAgreement()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
        }
    }

    private fun acceptAgreement() {
        viewModel.setStateEvent(AuthStateEvent.AcceptAgreementEvent)
    }

    private fun declineAgreement() {
        viewModel.setStateEvent(AuthStateEvent.DeclineAgreementEvent)
    }

    private fun resetAllFields() {
        viewModel.setSignUpStepOneFields(
            SignUpStepOneFields(
                "",
                ""
            )
        )
        viewModel.setSignUpStepTwoFields(
            SignUpStepTwoFields(
                ""
            )
        )
        viewModel.setSignUpStepThreeFields(
            SignUpStepThreeFields(
                "",
                ""
            )
        )

        val currentDate: Calendar = Calendar.getInstance()
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val month = currentDate.get(Calendar.MONTH)
        val year = currentDate.get(Calendar.YEAR) - 14
        currentDate.set(year, month, day)
        viewModel.setSignUpStepFourFields(
            SignUpFields(
                dateOfBirth = currentDate
            )
        )

        viewModel.setLoginFields(
            LoginFields(
                "",
                ""
            )
        )
    }

    private fun navPrivacyPolicy() {
        findNavController().navigate(R.id.action_agreementFragment_to_privacyPolicyFragment)
    }

    private fun navVerifyEmail() {
        findNavController().navigate(R.id.action_agreementFragment_to_verifyEmailFragment)
    }

    private fun navLauncher() {
        findNavController().navigate(R.id.action_agreementFragment_to_launcherFragment)
    }

    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.agreementPageLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.agreementPageLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}