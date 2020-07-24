package com.umbrellait.profile.presentation.registration_finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.umbrellait.profile.R
import com.umbrellait.profile.databinding.FragmentProfileRegistrationFinishedWithEmailBinding

class RegistrationFinishedWithEmailProfileFragment : Fragment() {

    private val args: RegistrationFinishedNoEmailProfileFragmentArgs by navArgs()

    private var _binding: FragmentProfileRegistrationFinishedWithEmailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegistrationFinishedProfileViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileRegistrationFinishedWithEmailBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textLabel = "${getString(R.string.confirm_your_email)} ${args.email}"
        binding.textLabelAboutApprove.text = textLabel

        binding.textSkipLabel.setOnClickListener {
            val directions = RegistrationFinishedWithEmailProfileFragmentDirections
                .actionStartWithEmailRegistrationFinishedNoEmailProfileFragment(
                    args.phoneNumber,
                    args.userName,
                    args.email)
            findNavController().navigate(directions)
        }
    }
}
