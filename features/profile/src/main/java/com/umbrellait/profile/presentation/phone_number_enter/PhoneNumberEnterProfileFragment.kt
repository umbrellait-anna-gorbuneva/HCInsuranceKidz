package com.umbrellait.profile.presentation.phone_number_enter

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.umbrellait.profile.R
import com.umbrellait.profile.databinding.FragmentProfileEnterPhoneNumberBinding

class PhoneNumberEnterProfileFragment : Fragment() {

    private var _binding: FragmentProfileEnterPhoneNumberBinding? = null

    /* This property is only valid between onCreateView and onDestroyView.*/
    private val binding get() = _binding!!

    private val viewModel by viewModels<PhoneNumberEnterProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileEnterPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.phoneNumberText.addTextChangedListener(phoneNumberWatcher)
        binding.continueButton.setOnClickListener { viewModel.buttonContinueClicked() }

        viewModel.enteredNumber.observe(viewLifecycleOwner, Observer { stringData ->
            with(binding.phoneNumberText) {
                removeTextChangedListener(phoneNumberWatcher)
                setText(stringData.first)
                setSelection(stringData.second)
                addTextChangedListener(phoneNumberWatcher)
            }
        })

        viewModel.buttonContinueState.observe(viewLifecycleOwner, Observer { active ->
            with(binding.continueButton) {
                when (active) {
                    true -> {
                        isClickable = true
                        setBackgroundResource(R.drawable.bckgrnd_button_gradient_rounded)
                        setTextColor(Color.WHITE)
                    }
                    else -> {
                        isClickable = false
                        setBackgroundResource(R.drawable.bckgrnd_button_gradient_rounded_gray)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            setTextColor(resources.getColor(R.color.colorSilver, null))
                        } else {
                            Color.DKGRAY
                        }
                    }
                }
            }
        })

        viewModel.catchCorrectPhoneNumber.observe(viewLifecycleOwner, Observer { event ->
            event?.handle {number ->
                val directions =
                    PhoneNumberEnterProfileFragmentDirections
                        .actionStartCodeEnterProfileFragment(number)
                findNavController().navigate(directions)
            }
        })

        viewModel.eventErrorNumberLength.observe(viewLifecycleOwner,
            Observer
            { shouldShowErrorMessage ->
                if (shouldShowErrorMessage) {
                    binding.phoneNumberInputLayout.error =
                        getString(R.string.number_should_contain)
                } else {
                    binding.phoneNumberInputLayout.error = null
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val phoneNumberWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(
            numberString: CharSequence?, start: Int, count: Int, after: Int
        ) {
        }

        override fun onTextChanged(
            numberString: CharSequence?, start: Int, before: Int, count: Int
        ) {
            viewModel.passNumber(numberString.toString(), start, before)
        }
    }


}
