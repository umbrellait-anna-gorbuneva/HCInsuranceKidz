package com.umbrellait.profile.presentation.code_enter

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.umbrellait.profile.R
import com.umbrellait.profile.databinding.FragmentProfileCodeEnterBinding
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots

class CodeEnterProfileFragment : Fragment() {

    private val args: CodeEnterProfileFragmentArgs by navArgs()

    private var _binding: FragmentProfileCodeEnterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CodeEnterProfileViewModel>()

    companion object {
        private const val TIME_IN_SECONDS_BEFORE_RESEND_SMS = 30L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileCodeEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startTimer(TIME_IN_SECONDS_BEFORE_RESEND_SMS)

        val phoneNumberString = args.userPhoneNumber
        val formattedPhoneNumber = formatNumberToMask(phoneNumberString)
        val textLabel = "${getString(R.string.sms_code_send_in_text)} $formattedPhoneNumber"
        binding.textSmsWasSent.text = textLabel

        binding.textInputSmsCode.doOnTextChanged { text, _, _, _ ->
            viewModel.codeEntered(text.toString())
        }

        binding.continueButton.setOnClickListener {
            viewModel.askSmsButtonClicked(
                TIME_IN_SECONDS_BEFORE_RESEND_SMS
            )
        }

        viewModel.eventErrorSmsCode.observe(viewLifecycleOwner,
            Observer { shouldShowErrorMessage ->
                if (shouldShowErrorMessage) {
                    binding.textErrorSmsCode.visibility = View.VISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.textInputSmsCode.setTextColor(resources.getColor(R.color.colorAccent, null))
                    } else {
                        Color.RED
                    }
                } else {
                    binding.textErrorSmsCode.visibility = View.INVISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.textInputSmsCode.setTextColor(resources.getColor(R.color.colorPrimaryDark, null))
                    } else {
                        Color.GRAY
                    }
                }
            })

        viewModel.countDownString.observe(viewLifecycleOwner, Observer { timeString ->
            val newTimeString = "${getString(R.string.new_code_available)} $timeString"
            binding.countDownText.text = newTimeString
        })

        viewModel.buttonState.observe(viewLifecycleOwner, Observer { activeState ->
            with(binding.continueButton) {
                when (activeState) {
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

        viewModel.catchCorrectSmsAnswer.observe(viewLifecycleOwner, Observer {value ->
            value?.handle {
                val directions =
                    CodeEnterProfileFragmentDirections
                        .actionStartEnterPersonalDataProfileFragment(args.userPhoneNumber)
                findNavController().navigate(directions)
                binding.textInputSmsCode.editableText.clear()
            }
        })
    }

    private fun formatNumberToMask(phoneNumberString: String): String {
        val numberToShow = phoneNumberString.removePrefix("+7 ").replace(" ", "")
        val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
        mask.insertFront(numberToShow)
        return mask.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}