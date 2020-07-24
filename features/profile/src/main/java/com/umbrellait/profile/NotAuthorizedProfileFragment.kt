package com.umbrellait.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.umbrellait.profile.databinding.FragmentProfileNotAuthorizedBinding

class NotAuthorizedProfileFragment : Fragment() {

    private var _binding: FragmentProfileNotAuthorizedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileNotAuthorizedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authOrRegisterButton.setOnClickListener { v: View? ->
            v?.findNavController()?.navigate(R.id.profileEnterPhoneNumberFragment)
        }
    }
}
