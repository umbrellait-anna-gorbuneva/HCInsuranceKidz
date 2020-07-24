package com.umbrellait.profile.presentation.phone_number_enter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umbrellait.core.livedata_helpers.ConsumableValue
import com.umbrellait.core.livedata_helpers.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots

class PhoneNumberEnterProfileViewModel : ViewModel() {

    companion object {
        private const val MAX_PHONE_NUMBER_LENGTH = 10
        // Сколько еще символов добавляется в номер, помимо цифр (без первой 7): "+", "7", 4 пробела
        private const val MASK_LENGTH = 6
        private const val POSITION_AFTER_PREFIX_IN_PHONE_NUMBER = 4
    }

    private val _catchCorrectPhoneNumber = MutableLiveData<ConsumableValue<String>>()
    val catchCorrectPhoneNumber: LiveData<ConsumableValue<String>> = _catchCorrectPhoneNumber

    private var realNumber: String = ""

    private val _eventErrorNumberLength = MutableLiveData<Boolean>()
    val eventErrorNumberLength: LiveData<Boolean> = _eventErrorNumberLength

    private val _buttonContinueVisible = MutableLiveData<Boolean>()
    val buttonContinueState: LiveData<Boolean> = _buttonContinueVisible

    private val _enteredNumber = MutableLiveData<Pair<String, Int>>()
    val enteredNumber: LiveData<Pair<String, Int>> = _enteredNumber

    init {
        _buttonContinueVisible.value = false
    }

    fun passNumber(phoneNumber: String, startPosition: Int, stepLeft: Int) {
        var errorsCounter = 0
        val cleanNumber = phoneNumber.replace(Regex("\\+7 \\(|\\W"), "").removePrefix("7")
        _buttonContinueVisible.value = cleanNumber.isNotEmpty()

//        Timber.d("MyLog_PhoneNumberEnterProfileViewModel_passNumber: ${cleanNumber.length}")
        if (cleanNumber.length > MAX_PHONE_NUMBER_LENGTH) {
            errorsCounter++
            showErrorWithNumberLength()
        } else {
            _eventErrorNumberLength.value = false
        }

        // Отсюда можно взять максу для расчета сдвигов курсора, нужно будет
        // для поключения других языков. См. Slots
        val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
        mask.insertFront(cleanNumber)

        // Определяю позицию курсора для возвращаемого номера
        var pos = when {
            // Когда удаляю цифры
            stepLeft > 0 -> {
                when {
                    startPosition < POSITION_AFTER_PREFIX_IN_PHONE_NUMBER -> {
                        POSITION_AFTER_PREFIX_IN_PHONE_NUMBER
                    }
                    else -> {
                        startPosition
                    }
                }
            }
            // Когда добавляю цифры
            else -> {
                if (errorsCounter > 0 && startPosition < mask.toString().length) {
                    // Надо учесть сдвиги из-за маски. Вероятно, можно улучишь код используя данные из MaskImpl (Slots и пр.)
                    when (startPosition) {
                        6 -> {
                            9
                        }
                        11 -> {
                            13
                        }
                        14 -> {
                            16
                        }
                        else -> {
                            (startPosition + (mask.toString().length - phoneNumber.length) + 2)
                        }
                    }
                } else {
                    (startPosition + (mask.toString().length - phoneNumber.length) + 1)
                }
            }
        }

        // На случай вставки из буфера большого количества сивмволов сразу
        if (pos > mask.toString().length) {
            pos = mask.toString().length
        }

        realNumber = mask.toString().replace(Regex("[()]"), "").replace(Regex("-"), " ")
        _enteredNumber.value = mask.toString() to (pos)
    }

    private fun showErrorWithNumberLength() {
        _eventErrorNumberLength.value = true
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(1500)
            _eventErrorNumberLength.value = false
        }
    }

    fun buttonContinueClicked() {
        if (realNumber.length == MAX_PHONE_NUMBER_LENGTH + MASK_LENGTH) {
            _catchCorrectPhoneNumber.value = ConsumableValue(realNumber)
        } else {
            showErrorWithNumberLength()
        }
    }

}