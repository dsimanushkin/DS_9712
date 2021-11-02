package com.ds9712.ds_9712.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.FragmentChangeEmailAddressBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.state.AuthStateEvent
import com.ds9712.ds_9712.ui.auth.state.ChangeEmailAddressFields
import com.ds9712.ds_9712.util.ErrorHandling
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class ChangeEmailAddressFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
): BaseAuthFragment(viewModelFactory) {

    private var _binding: FragmentChangeEmailAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeEmailAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        setOnClickListeners()
        setOnTextChangedListeners()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.changeEmailAddressFields?.let { changeEmailAddressFields ->
                changeEmailAddressFields.changeEmailAddressEmail?.let {
                    binding.changeEmailPageEditTextEmail.setText(it)
                }
                changeEmailAddressFields.changeEmailAddressPassword?.let {
                    binding.changeEmailPageEditTextPassword.setText(it)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == 9) {
                    if(stateMessage.response.statusCode == 2017){
                        uiCommunicationListener.displaySnackBarMessage(ErrorHandling.handleErrors(2017, requireActivity().application))
                        activity?.onBackPressed()
                        clearChangeEmailAddressFields()
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
        binding.changeEmailPageBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.changeEmailPageSubmitButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                if (binding.changeEmailPageEditTextEmail.text.isNullOrEmpty()) {
                    binding.changeEmailPageEditTextEmail.requestFocus()
                } else if (binding.changeEmailPageEditTextPassword.text.isNullOrEmpty()) {
                    binding.changeEmailPageEditTextPassword.requestFocus()
                }
                changeEmailAddress()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
            uiCommunicationListener.hideSoftKeyboard()
        }
    }

    private fun setOnTextChangedListeners() {
        binding.changeEmailPageEditTextEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.changeEmailPageEditTextPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun changeEmailAddress() {
        viewModel.setStateEvent(
            AuthStateEvent.ChangeEmailAddressEvent(
            binding.changeEmailPageEditTextEmail.text.toString(),
            binding.changeEmailPageEditTextPassword.text.toString()
        ))
    }

    private fun saveChangeEmailAddressFields() {
        viewModel.setChangeEmailAddressFields(
            ChangeEmailAddressFields(
                binding.changeEmailPageEditTextEmail.text.toString(),
                binding.changeEmailPageEditTextPassword.text.toString()
            )
        )
    }

    private fun clearChangeEmailAddressFields() {
        binding.changeEmailPageEditTextEmail.setText("")
        binding.changeEmailPageEditTextPassword.setText("")
        viewModel.setChangeEmailAddressFields(
            ChangeEmailAddressFields(
                "",
                ""
            )
        )
    }

    private fun setErrorMessage(errorMessage: String) {
        binding.changeEmailPageTextInputLayoutPassword.helperText = errorMessage
    }

    private fun clearErrorMessage() {
        binding.changeEmailPageTextInputLayoutPassword.helperText = null
    }

    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.changeEmailPageLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.changeEmailPageLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveChangeEmailAddressFields()
        _binding = null
    }

}