package khudiakov.kirill.calculator

import java.math.BigDecimal
import java.util.*

/**
 * Class that encapsulates all arithmetic operations
 */
class Calculator {

    val operators = listOf('+', '-', '×', '÷', '%').toCharArray()

    /**
     * Calculates given expression. This method uses Reverse Polish notation algorithm.
     * @param expression Arithmetical expression.
     * @return Nullable string representation of result.
     */
    fun calculate(expression: String?): String? {
        if (expression == null || expression.isEmpty()) return null
        val replacedExpression = expression.replace(',', '.')

        val tokens = parseExpression(replacedExpression).toMutableList()
        if (tokens.isEmpty()) return null

        val numbers = Stack<BigDecimal>()
        val operators = Stack<Char>()

        for ((index, token) in tokens.withIndex()) {
            if (isUnaryOperator(tokens, index)) {
                val next = index + 1
                if (next < tokens.size) {
                    tokens[next] = token + tokens[next]
                    tokens.removeAt(index)
                }
            } else if (isOperator(token)) {
                val operator = token[0]
                while (operators.isNotEmpty() &&
                    operatorPriority(operators.peek()) >= operatorPriority(operator)
                ) {
                    val currentOperator = operators.pop()
                    val right = numbers.pop()
                    val left = numbers.pop()
                    numbers.push(calculate(left, currentOperator, right))
                }
                operators.push(operator)
            } else {
                numbers.push(token.toBigDecimal())
            }
        }

        while (operators.isNotEmpty()) {
            val operator = operators.pop()
            val right = numbers.pop()
            val left = numbers.pop()
            numbers.push(calculate(left, operator, right))
        }

        require(numbers.size == 1) { "Wrong arithmetic expression" }
        return numbers.pop().toString().replace('.', ',')
    }

    /**
     * Apply arithmetical operator to two values.
     * @param left First value.
     * @param right Second value.
     * @return operation result.
     */
    private fun calculate(left: BigDecimal, operator: Char, right: BigDecimal): BigDecimal {
        return when (operator) {
            '+' -> left + right
            '-' -> left - right
            '×' -> left * right
            '÷' -> left / right
            '%' -> left * right / BigDecimal.valueOf(100)
            else -> throw IllegalArgumentException("Unknown operator")
        }
    }

    /**
     * Parse arithmetical expression string to list of tokens.
     * Token - separate string that can be either number or operator.
     * @param expression String representation of arithmetical expression.
     * @return list of tokens.
     */
    private fun parseExpression(expression: String): List<String> {
        if (expression.length == 1) {
            return listOf(expression)
        }

        val tokens = mutableListOf<String>()
        var startIndex = 0
        var endIndex = startIndex + 1


        while (endIndex <= expression.length) {
            if (isDigit(expression[startIndex])) {
                while (endIndex < expression.length && isDigit(expression[endIndex])) ++endIndex
                val token = expression.substring(
                    startIndex,
                    if (endIndex == expression.length - 1 && isDigit(expression[endIndex]))
                        endIndex + 1
                    else
                        endIndex
                )
                tokens.add(token)
            } else if (isOperator(expression[startIndex])) {
                tokens.add(expression[startIndex].toString())
            }
            startIndex = endIndex++
        }

        return tokens
    }

    fun isDigit(char: Char): Boolean {
        return char in listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.')
    }

    private fun isOperator(char: Char): Boolean {
        return char in operators
    }

    private fun isOperator(string: String): Boolean {
        return string in listOf("+", "-", "×", "÷", "%")
    }

    @JvmName("isUnaryOperatorTokens")
    fun isUnaryOperator(tokens: List<String>, index: Int): Boolean {
        return (tokens[index] == "-" || tokens[index] == "+") &&
                (index == 0 || isOperator(tokens[index - 1]))
    }

    @JvmName("isUnaryOperatorChars")
    fun isUnaryOperator(chars: List<Char>, index: Int): Boolean {
        return (chars[index] == '-' || chars[index] == '+') &&
                (index == 0 || isOperator(chars[index - 1]))
    }

    /**
     * Determine priority for arithmetical operators.
     * @param operator Operator character.
     * @return integer value from 1 to 3.
     */
    private fun operatorPriority(operator: Char): Int {
        return when (operator) {
            '+', '-' -> 1
            '×', '÷' -> 2
            '%' -> 3
            else -> throw IllegalArgumentException("Unknown operator")
        }
    }
}