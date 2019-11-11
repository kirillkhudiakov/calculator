package khudiakov.kirill.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var previous: Int? = null
    private var number: Int = 0

    private val _result = MutableLiveData(0)
    val result: LiveData<Int>
        get() = _result

    fun onButtonClicked(button: String) {
        when {
            button.isDigit() -> {
                val digit = button.toInt()
                number = number * 10 + digit
                _result.value = number
            }

        }
    }
}

private fun String.isDigit(): Boolean {
    return this == "0" || this == "1" || this == "2" || this == "3" || this == "4" || this == "5"
            || this == "6" || this == "7" || this == "8" || this == "9"
}