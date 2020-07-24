package com.umbrellait.profile.presentation.registration_finished

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.umbrellait.profile.R
import com.umbrellait.profile.databinding.FragmentProfileEnterPersonalDataBinding
import com.umbrellait.profile.databinding.FragmentProfileRegistrationFinishedNoEmailBinding
import com.umbrellait.profile.presentation.enter_personal_data.EnterPersonalDataProfileFragmentArgs
import com.umbrellait.profile.presentation.enter_personal_data.EnterPersonalDataViewModel
import timber.log.Timber

class RegistrationFinishedNoEmailProfileFragment : Fragment() {

    private val args: RegistrationFinishedNoEmailProfileFragmentArgs by navArgs()

    private var _binding: FragmentProfileRegistrationFinishedNoEmailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegistrationFinishedProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileRegistrationFinishedNoEmailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registrationButton.setOnClickListener {
            Timber.d("MyLog_RegistrationFinishedNoEmailProfileFragment_onViewCreated: !!!")
        }
    }
}
