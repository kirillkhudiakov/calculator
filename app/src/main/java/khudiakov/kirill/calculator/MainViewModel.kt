package khudiakov.kirill.calculator

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val calculator = Calculator()

    private val _result = MutableLiveData<String>()
    val result: LiveData<String>
        get() = _result

    private val _expression = MutableLiveData<String>()
    val expression: LiveData<String>
        get() = _expression

    private var lastCalculatedExpression: String? = null

    /**
     * Click listener for each calculator button.
     * @param symbol Selected symbol.
     */
    fun onButtonClicked(symbol: String) {
        when (symbol) {
            "=" -> calculate()
            "AC" -> clearAll()
            "+/-" -> onPlusMinusClicked()
            else -> appendToExpression(symbol)
        }
    }

    /**
     * Try to calculate current expression. Displays result if success, otherwise shows error.
     */
    private fun calculate() {
        try {
            if (_result.value == null || _expression.value != lastCalculatedExpression) {
                _result.value = calculator.calculate(_expression.value).toString()
            } else if (_result.value != ERROR_STRING) {
                getLastOperation()?.let {
                    _expression.value += getLastOperation()
                    _result.value = calculator.calculate(_expression.value).toString()
                }
            }
        } catch (e: Exception) {
            _result.value = ERROR_STRING
            _expression.value = null
        }
        finally {
            lastCalculatedExpression = _expression.value
        }
    }

    /**
     * Clear current expression and result.
     */
    private fun clearAll() {
        _expression.value = null
        _result.value = null
    }

    /**
     * Change sign of the last number in expression.
     */
    private fun onPlusMinusClicked() {
        val value = _expression.value?.toMutableList() ?: return
        var index = value.size - 1
        while (index >= 0 && calculator.isDigit(value[index])) --index

        if (index == -1) {
            value.add(0, '-')
        } else if (value[index] == '+') {
            value[index] = '-'
        } else if (value[index] == '-') {
            if (calculator.isUnaryOperator(value, index)) {
                value.removeAt(index)
            } else {
                value[index] = '+'
            }
        } else {
            value.add(index + 1, '-')
        }

        _expression.value = value.joinToString("")
    }

    /**
     * Add symbol to current expression.
     * @param symbol New symbol.
     */
    private fun appendToExpression(symbol: String) {
        _expression.value = _expression.value?.plus(symbol) ?: symbol
    }

    /**
     * Gets last operation from arithmetical expression.
     */
    private fun getLastOperation(): String? {
        val value = _expression.value ?: return null
        var index = value.lastIndexOfAny(calculator.operators)

        if (index == -1) {
            return null
        }
        if (calculator.isUnaryOperator(value.toList(), index) && index > 0) {
            --index
        }

        return value.substring(index)
    }

    companion object {
        private const val ERROR_STRING = "Invalid input"
    }
}