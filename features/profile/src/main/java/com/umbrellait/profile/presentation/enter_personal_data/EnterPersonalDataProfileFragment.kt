package com.umbrellait.profile.presentation.enter_personal_data

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.umbrellait.profile.R
import com.umbrellait.profile.databinding.FragmentProfileEnterPersonalDataBinding
import timber.log.Timber

class EnterPersonalDataProfileFragment : Fragment() {

    private val args: EnterPersonalDataProfileFragmentArgs by navArgs()

    private var _binding: FragmentProfileEnterPersonalDataBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EnterPersonalDataViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileEnterPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("MyLog_EnterPersonalDataProfileFragment_onViewCreated: ${args.userPhoneNumber}")

        viewModel.buttonContinueRegState.observe(viewLifecycleOwner, Observer { isActive ->
            with(binding.registrationButton) {
                when (isActive) {
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

        binding.nameEditText.doOnTextChanged { text, start, count, after ->
            viewModel.nameTextFiledEntered(text)
        }

        binding.registrationButton.setOnClickListener {
            viewModel.validateEmail(binding.emailEditText.text)
        }

        viewModel.eventErrorEmail.observe(viewLifecycleOwner, Observer { errorEmailEvent ->
            if (errorEmailEvent) {
                binding.emailFieldInputLayout.error = getString(R.string.invalid_email)
            } else {
                binding.emailFieldInputLayout.error = null
            }
        })

        viewModel.setCorrectPersonalData.observe(viewLifecycleOwner,
            Observer { event ->
                event.handle { email ->
                    val directions: NavDirections
                    if (email.isEmpty()) {
                        directions = EnterPersonalDataProfileFragmentDirections.actionStartRegistrationFinishedNoEmailProfileFragment(
                            args.userPhoneNumber,
                            binding.nameEditText.toString(),
                            ""
                        )

                    } else {
                        directions =
                            EnterPersonalDataProfileFragmentDirections
                                .actionStartRegistrationFinishedWithEmailProfileFragment(
                                    args.userPhoneNumber,
                                    binding.nameEditText.toString(),
                                    email
                                )
                    }
                    findNavController().navigate(directions)
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
