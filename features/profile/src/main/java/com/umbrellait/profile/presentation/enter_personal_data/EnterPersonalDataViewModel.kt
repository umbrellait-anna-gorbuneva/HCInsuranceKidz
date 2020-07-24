package com.umbrellait.profile.presentation.enter_personal_data

import android.text.Editable
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umbrellait.core.livedata_helpers.ConsumableValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EnterPersonalDataViewModel : ViewModel() {

    private val _buttonContinueRegVisible = MutableLiveData<Boolean>()
    val buttonContinueRegState: LiveData<Boolean> = _buttonContinueRegVisible

    private val _eventErrorEmail = MutableLiveData<Boolean>()
    val eventErrorEmail: LiveData<Boolean> = _eventErrorEmail

    private val _setCorrectPersonalData = MutableLiveData<ConsumableValue<String>>()
    val setCorrectPersonalData: LiveData<ConsumableValue<String>> = _setCorrectPersonalData


    init {
        _buttonContinueRegVisible.value = false
        _eventErrorEmail.value = false
    }

    fun nameTextFiledEntered(text: CharSequence?) {
        _buttonContinueRegVisible.value = !text.isNullOrEmpty()
    }

    fun validateEmail(target: Editable?) {
        if (target?.isEmpty()!!) {
            _setCorrectPersonalData.value = ConsumableValue("")
            return
        }
        val emailIsValid =
            !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches()
        if (emailIsValid) {
            _setCorrectPersonalData.value = ConsumableValue(target.toString())
        } else {
            showErrorWithEmail()
        }
    }

    private fun showErrorWithEmail() {
        _eventErrorEmail.value = true
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(1500)
            _eventErrorEmail.value = false
        }
    }


}