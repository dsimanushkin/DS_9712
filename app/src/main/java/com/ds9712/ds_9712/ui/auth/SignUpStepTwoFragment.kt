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
import com.ds9712.ds_9712.databinding.FragmentSignUpStepTwoBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.state.AuthStateEvent
import com.ds9712.ds_9712.ui.auth.state.SignUpStepTwoFields
import com.ds9712.ds_9712.util.ErrorHandling
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class SignUpStepTwoFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
): BaseAuthFragment(viewModelFactory) {
    private var _binding: FragmentSignUpStepTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStepTwoBinding.inflate(inflater, container, false)
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
            viewState.signUpStepTwoFields?.let { signUpStepTwoFields ->
                signUpStepTwoFields.signUpEmail?.let {
                    binding.signUpEmailPageEditTextEmail.setText(it)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == 4) {
                    if(stateMessage.response.statusCode == 1024){
                        saveStepTwoFields()
                        navSignUpStepThree()
                    } else if (stateMessage.response.statusCode == -2) {
                        setErrorMessage(stateMessage.response.message.toString())
                    } else {
                        setErrorMessage(ErrorHandling.handleErrors(stateMessage.response.statusCode, requireActivity().application))
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
        binding.signUpEmailPageBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.signUpEmailPageNextButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                if (binding.signUpEmailPageEditTextEmail.text.isNullOrEmpty()) {
                    binding.signUpEmailPageEditTextEmail.requestFocus()
                }
                checkSignUpStepTwoFields()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
        }
    }

    private fun setOnTextChangedListeners() {
        binding.signUpEmailPageEditTextEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun setErrorMessage(errorMessage: String) {
        binding.signUpEmailPageTextInputLayoutEmail.helperText = errorMessage
    }

    private fun clearErrorMessage() {
        binding.signUpEmailPageTextInputLayoutEmail.helperText = null
    }

    private fun checkSignUpStepTwoFields() {
        viewModel.setStateEvent(
            AuthStateEvent.ValidateSignUpStepTwoFieldsEvent(
                binding.signUpEmailPageEditTextEmail.text.toString()
            ))
    }

    private fun navSignUpStepThree() {
        findNavController().navigate(R.id.action_signUpStepTwoFragment_to_signUpStepThreeFragment)
    }

    private fun saveStepTwoFields() {
        viewModel.setSignUpStepTwoFields(
            SignUpStepTwoFields(
                binding.signUpEmailPageEditTextEmail.text.toString()
            )
        )
    }

    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.signUpEmailPageLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.signUpEmailPageLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveStepTwoFields()
        _binding = null
    }
}