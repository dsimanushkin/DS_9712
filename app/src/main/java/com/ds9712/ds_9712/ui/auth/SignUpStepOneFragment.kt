package com.ds9712.ds_9712.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.FragmentSignUpStepOneBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.state.AuthStateEvent
import com.ds9712.ds_9712.ui.auth.state.SignUpStepOneFields
import com.ds9712.ds_9712.util.ErrorHandling
import com.ds9712.ds_9712.util.ErrorHandling.Companion.handleErrors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class SignUpStepOneFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
): BaseAuthFragment(viewModelFactory) {
    private var _binding: FragmentSignUpStepOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStepOneBinding.inflate(inflater, container, false)
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
            viewState.signUpStepOneFields?.let { signUpStepOneFields ->
                signUpStepOneFields.signUpFullName?.let {
                    binding.signUpNameUsernamePageEditTextFullName.setText(it)
                }
                signUpStepOneFields.signUpUsername?.let {
                    binding.signUpNameUsernamePageEditTextUsername.setText(it)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == 3) {
                    if(stateMessage.response.statusCode == 1015){
                        saveStepOneFields()
                        navSignUpStepTwo()
                    } else if (stateMessage.response.statusCode == -2) {
                        setErrorMessage(stateMessage.response.message.toString())
                    } else {
                        setErrorMessage(handleErrors(stateMessage.response.statusCode, requireActivity().application))
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
        binding.signUpNameUsernamePageBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.signUpNameUsernamePageSignInButton.setOnClickListener {
            navLogin()
        }

        binding.signUpNameUsernamePageNextButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                if (binding.signUpNameUsernamePageEditTextFullName.text.isNullOrEmpty()) {
                    binding.signUpNameUsernamePageEditTextFullName.requestFocus()
                } else if (binding.signUpNameUsernamePageEditTextUsername.text.isNullOrEmpty()) {
                    binding.signUpNameUsernamePageEditTextUsername.requestFocus()
                }
                checkSignUpStepOneFields()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
            uiCommunicationListener.hideSoftKeyboard()
        }
    }

    private fun setOnTextChangedListeners() {
        binding.signUpNameUsernamePageEditTextFullName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.signUpNameUsernamePageEditTextUsername.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setErrorMessage(errorMessage: String) {
        binding.signUpNameUsernamePageTextInputLayoutUsername.helperText = errorMessage
    }

    private fun clearErrorMessage() {
        binding.signUpNameUsernamePageTextInputLayoutUsername.helperText = null
    }

    private fun checkSignUpStepOneFields() {
        viewModel.setStateEvent(
            AuthStateEvent.ValidateSignUpStepOneFieldsEvent(
                binding.signUpNameUsernamePageEditTextFullName.text.toString(),
                binding.signUpNameUsernamePageEditTextUsername.text.toString()
            ))
    }

    private fun saveStepOneFields() {
        viewModel.setSignUpStepOneFields(
            SignUpStepOneFields(
                binding.signUpNameUsernamePageEditTextFullName.text.toString(),
                binding.signUpNameUsernamePageEditTextUsername.text.toString()
            )
        )
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_signUpStepOneFragment_to_loginFragment)
    }

    private fun navSignUpStepTwo() {
        findNavController().navigate(R.id.action_signUpStepOneFragment_to_signUpStepTwoFragment)
    }

    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.signUpNameUsernamePageLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.signUpNameUsernamePageLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveStepOneFields()
        _binding = null
    }
}