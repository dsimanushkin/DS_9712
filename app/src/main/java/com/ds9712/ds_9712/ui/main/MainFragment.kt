package com.ds9712.ds_9712.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.FragmentMainBinding
import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.models.CurrentQuestion
import com.ds9712.ds_9712.ui.main.state.MainStateEvent
import com.ds9712.ds_9712.util.ErrorHandling
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@MainScope
class MainFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
): BaseMainFragment(viewModelFactory) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var requestManager: RequestManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGlide()
        subscribeObservers()

        if (ErrorHandling.isInternetAvailable(requireContext())) {
            callApiForCurrentQuestion()
        } else {
            showErrorMessage(resources.getString(R.string.main_error_no_internet_connection))
        }

        setOnClickListeners()
        setOnTextChangedListeners()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.currentQuestionFields.currentQuestion?.let {
                    showBigProgressBar(false)
                    initCurrentQuestion(it)

                    viewState.currentQuestionFields.currentQuestion = null
                }
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let {
                when (stateMessage.response.requestCode) {
                    1 -> {
                        when(stateMessage.response.statusCode) {
                            1002 -> { // User solved all question.
                                // Show final screen
                                navFinalScreen()
                                viewModel.clearStateMessage()
                            }
                            1004 -> { // Found matching key.
                                showMiniProgressBar(false)
                                binding.keyEditText.setText("")
                                viewModel.clearStateMessage()
                                callApiForCurrentQuestion()
                            }
                            1005 -> { // You entered an invalid key.
                                setErrorMessage(resources.getString(R.string.main_error_invalid_key))
                                showMiniProgressBar(false)
                                viewModel.clearStateMessage()
                            }
                            else -> {
                                showErrorMessage(resources.getString(R.string.main_error_unable_to_load_content))
                                showMiniProgressBar(false)
                                showBigProgressBar(false)
                                viewModel.clearStateMessage()
                            }
                        }
                    }
                    2 -> {
                        when(stateMessage.response.statusCode) {
                            1002 -> {
                                // Show final screen
                                navFinalScreen()
                                viewModel.clearStateMessage()
                            }
                            else -> {
                                showErrorMessage(resources.getString(R.string.main_error_unable_to_load_content))
                                showMiniProgressBar(false)
                                showBigProgressBar(false)
                                viewModel.clearStateMessage()
                            }
                        }
                    }
                    else -> {
                        showErrorMessage(resources.getString(R.string.main_error_unable_to_load_content))
                        showMiniProgressBar(false)
                        showBigProgressBar(false)
                        viewModel.clearStateMessage()
                    }
                }
            }
        })
    }

    private fun setOnClickListeners() {
        binding.errorRetryButton.setOnClickListener {
            callApiForCurrentQuestion()
            binding.errorMessageContainer.visibility = View.GONE
        }

        binding.sendButton.setOnClickListener {
            showMiniProgressBar(true)

            val key = binding.keyEditText.text.toString()
            if (key.isEmpty()) {
                setErrorMessage(resources.getString(R.string.main_error_key_can_not_be_empty))
                showMiniProgressBar(false)
            } else {
                callApiToSolveQuestion(key)
            }
        }
    }

    private fun navFinalScreen() {
        findNavController().navigate(R.id.action_mainFragment_to_finalFragment)
    }

    private fun setOnTextChangedListeners() {
        binding.keyEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearErrorMessage()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setErrorMessage(errorMessage: String) {
        binding.keyTextInputLayout.helperText = errorMessage
    }

    private fun clearErrorMessage() {
        binding.keyTextInputLayout.helperText = null
    }

    private fun callApiForCurrentQuestion() {
        showBigProgressBar(true)
        viewModel.setStateEvent(MainStateEvent.GetCurrentQuestionEvent)
    }

    private fun callApiToSolveQuestion(userKey: String) {
        viewModel.setStateEvent(MainStateEvent.SolveQuestionEvent(userKey))
    }

    private fun initCurrentQuestion(currentQuestion: CurrentQuestion) {
        binding.questionId.text = currentQuestion.qId
        binding.questionTitle.text = currentQuestion.qTitle
        requestManager!!
            .load(currentQuestion.qImage)
            .into(binding.questionImage)
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.errorMessage.text = errorMessage
        binding.errorMessageContainer.visibility = View.VISIBLE
    }

    private fun showMiniProgressBar(visibility: Boolean) {
        if (visibility) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showBigProgressBar(visibility: Boolean) {
        if (visibility) {
            binding.progressBarContainer.visibility = View.VISIBLE
        } else {
            binding.progressBarContainer.visibility = View.GONE
        }
    }

    private fun setupGlide(){
        val requestOptions = RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)

        requestManager = Glide.with(this)
            .applyDefaultRequestOptions(requestOptions)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}