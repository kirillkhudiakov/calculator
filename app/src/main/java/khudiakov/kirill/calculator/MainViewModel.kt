package khudiakov.kirill.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var previousNumber: Int? = null
    private var previousOperator: String? = null
    private var number: Int? = 0

    private val _result = MutableLiveData(0)
    val result: LiveData<Int>
        get() = _result

    private val _history = MutableLiveData<String>()
    val history: LiveData<String>
        get() = _history

    fun onButtonClicked(symbol: String) {
        when(symbol) {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> onDigitClicked(symbol)
            "+", "-", "×", "÷", "%" -> onOperatorClicked(symbol)
            "=" -> onEqualClicked()
            "AC" -> onAllClearClicked()
            "+/-" -> onPlusMinusClicked()
        }
    }

    private fun onDigitClicked(digit: String) {

    }

    private fun onOperatorClicked(operator: String) {

    }

    private fun onEqualClicked() {

    }

    private fun onAllClearClicked() {

    }

    private fun onPlusMinusClicked() {

    }

    private fun appendToHistory(symbol: String) {
        _history.value = _history.value?.plus(symbol) ?: symbol
    }

    private fun calculate(a: Int, operator: String, b: Int): Int {
        return when (operator) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> a / b
            else -> throw IllegalArgumentException("Unknown operator")
        }
    }
}