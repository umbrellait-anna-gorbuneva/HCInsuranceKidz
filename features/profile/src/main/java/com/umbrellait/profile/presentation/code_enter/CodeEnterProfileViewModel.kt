package com.umbrellait.profile.presentation.code_enter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umbrellait.core.livedata_helpers.ConsumableValue
import com.umbrellait.core.livedata_helpers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CodeEnterProfileViewModel : ViewModel() {

    private val correctSms = "222222"

    //TODO Сделать счетчики: попыток ввода кода СМС, количество попыток за день, за 5 минут, и т.п.
    //  Ограничивать работу формы в зависимости от счетчиков

//    private val _setCorrectAnswer = MutableLiveData<Event<String>>()
//    val setCorrectAnswer: LiveData<Event<String>> = _setCorrectAnswer

    private val _eventErrorSmsCode = MutableLiveData<Boolean>()
    val eventErrorSmsCode: LiveData<Boolean> = _eventErrorSmsCode

    private val _buttonState = MutableLiveData<Boolean>()
    val buttonState: LiveData<Boolean> = _buttonState

    private val _countDownString = MutableLiveData<String>()
    val countDownString: LiveData<String> = _countDownString

    private val _catchCorrectSmsAnswer = MutableLiveData<ConsumableValue<Boolean>>()
    val catchCorrectSmsAnswer: LiveData<ConsumableValue<Boolean>> = _catchCorrectSmsAnswer


    companion object {
        private const val SMS_LENGTH = 6
    }

    init {
        _buttonState.value = false
    }

    fun codeEntered(text: String) {
        if (text.length == SMS_LENGTH) {
            if (text == correctSms) {
                Timber.d("MyLog_CodeEnterProfileViewModel_codeEntered: GO NEXT SCREEN!")
                _catchCorrectSmsAnswer.value = ConsumableValue(true)
            } else {
                _eventErrorSmsCode.value = true
            }
        } else {
            _eventErrorSmsCode.value = false
        }
    }

    fun startTimer(totalSeconds: Long) {
        GlobalScope.launch(Dispatchers.Main) {
//            val totalSeconds = TimeUnit.MINUTES.toSeconds(2)
            val tickSeconds = 1
            for (second in totalSeconds downTo tickSeconds) {
                val time = String.format(
                    "%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(second),
                    second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
                )
                _countDownString.value = time
                delay(1000)
            }
            _countDownString.value = "00:00"
            // Активирую кнопку для повторного запроса СМС
            _buttonState.value = true
        }
    }

    fun askSmsButtonClicked(seconds: Long) {
        //TODO Запросить СМС повторно
        _buttonState.value = false
        startTimer(seconds)
    }
}