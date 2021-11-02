package com.ds9712.ds_9712.ui.auth

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.FragmentSignUpStepFourBinding
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.state.AuthStateEvent
import com.ds9712.ds_9712.ui.auth.state.SignUpFields
import com.ds9712.ds_9712.util.ErrorHandling
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class SignUpStepFourFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
): BaseAuthFragment(viewModelFactory) {

    private var _binding: FragmentSignUpStepFourBinding? = null
    private val binding get() = _binding!!

    private var fullName: String? = null
    private var username: String? = null
    private var email: String? = null
    private var password: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStepFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatePicker()
        subscribeObservers()
        setOnClickListeners()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.signUpFields?.let { signUpFields ->
                signUpFields.dateOfBirth?.let {
                    binding.signUpDobDatepicker.updateDate(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(
                        Calendar.DAY_OF_MONTH))
                }
            }
            viewState.signUpStepOneFields?.let { signUpStepOneFields ->
                signUpStepOneFields.signUpFullName?.let {
                    fullName = it
                }
                signUpStepOneFields.signUpUsername?.let {
                    username = it
                }
            }
            viewState.signUpStepTwoFields?.let { signUpStepTwoFields ->
                signUpStepTwoFields.signUpEmail?.let {
                    email = it
                }
            }
            viewState.signUpStepThreeFields?.let { signUpStepThreeFields ->
                signUpStepThreeFields.signUpPassword?.let {
                    password = it
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer {stateMessage ->
            stateMessage?.let {
                if (stateMessage.response.requestCode == 6) {
                    if (stateMessage.response.statusCode == 2001) {
                        navAgreement()
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
        binding.signUpDobPageBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.signUpDobPageNextButton.setOnClickListener {
            if (ErrorHandling.isInternetAvailable(requireContext())) {
                signUp()
            } else {
                uiCommunicationListener.displaySnackBarMessage(getString(R.string.no_internet_connection_description))
            }
        }
    }

    private fun navAgreement() {
        findNavController().navigate(R.id.action_signUpStepFourFragment_to_agreementFragment)
    }

    private fun signUp() {
        if (!fullName.isNullOrEmpty() && !username.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val dateOfBirth: Calendar = Calendar.getInstance()
            dateOfBirth.set(binding.signUpDobDatepicker.year, binding.signUpDobDatepicker.month, binding.signUpDobDatepicker.dayOfMonth)
            viewModel.setStateEvent(
                AuthStateEvent.SignUpEvent(
                fullName = fullName!!,
                username = username!!,
                email = email!!,
                password = password!!,
                dateOfBirth = dateOfBirth
            ))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDatePicker() {
        val vibrator: Vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val currentDate: Calendar = Calendar.getInstance()
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val month = currentDate.get(Calendar.MONTH)
        val year = currentDate.get(Calendar.YEAR) - 18
        displaySelectedDate(year, month, day)
        binding.signUpDobDatepicker.maxDate = currentDate.timeInMillis
        binding.signUpDobDatepicker.updateDate(year, month, day)
        binding.signUpDobDatepicker.setOnDateChangedListener { datePicker, _, _, _ ->
            displaySelectedDate(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                vibrator.vibrate(5)
            }
        }
    }

    private fun displaySelectedDate(year: Int, month: Int, day: Int) {
        val months = resources.getStringArray(R.array.months)
        val completeDate = "${months[month]} $day, $year"
        binding.signUpDobDatepickerText.text = completeDate
    }

    private fun saveStepFourFields() {
        val dateOfBirth: Calendar = Calendar.getInstance()
        dateOfBirth.set(binding.signUpDobDatepicker.year, binding.signUpDobDatepicker.month, binding.signUpDobDatepicker.dayOfMonth)
        viewModel.setSignUpStepFourFields(
            SignUpFields(
                dateOfBirth = dateOfBirth
            )
        )
    }



    private fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.signUpDobPageLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.signUpDobPageLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveStepFourFields()
        _binding = null
    }
}