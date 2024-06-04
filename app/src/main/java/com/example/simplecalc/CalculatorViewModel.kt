package com .example.simplecalc
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.*

class CalculatorViewModel : ViewModel() {
    val result = MutableLiveData<String>()
    val history = MutableLiveData<String>()

    private var currentInput = ""
    private val historyBuilder = StringBuilder()

    init {
        result.value = "0"
        history.value = ""
    }

    fun onDigitClick(digit: String) {
        currentInput += digit
        result.value = currentInput
    }

    fun onOperatorClick(operator: String) {
        if (currentInput.isNotEmpty()) {
            currentInput += " $operator "
            result.value = currentInput
        }
    }

    fun onEqualClick() {
        try {
            val expression = currentInput.replace("^", "**")
            val evaluatedResult = eval(expression)
            historyBuilder.append("$currentInput = $evaluatedResult\n")
            history.value = historyBuilder.toString()
            result.value = evaluatedResult.toString()
            currentInput = evaluatedResult.toString()
        } catch (e: Exception) {
            result.value = "Error"
        }
    }

    fun onClearClick() {
        currentInput = ""
        result.value = "0"
    }

    fun onParenthesisClick(parenthesis: String) {
        currentInput += parenthesis
        result.value = currentInput
    }

    fun onDecimalClick() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            result.value = currentInput
        }
    }

    fun onFunctionClick(function: String) {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDouble()
            val resultValue = when (function) {
                "sin" -> sin(Math.toRadians(value))
                "cos" -> cos(Math.toRadians(value))
                "tan" -> tan(Math.toRadians(value))
                "sqrt" -> sqrt(value)
                "log" -> log10(value)
                else -> 0.0
            }
            result.value = resultValue.toString()
            historyBuilder.append("$function($currentInput) = $resultValue\n")
            history.value = historyBuilder.toString()
            currentInput = resultValue.toString()
        }
    }

    private fun eval(expr: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expr.length) expr[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm() // addition
                    else if (eat('-'.toInt())) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor() // multiplication
                    else if (eat('/'.toInt())) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor() // unary plus
                if (eat('-'.toInt())) return -parseFactor() // unary minus

                var x: Double
                val startPos = pos
                if (eat('('.toInt())) { // parentheses
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) { // numbers
                    while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = expr.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.toInt() && ch <= 'z'.toInt()) { // functions
                    while (ch >= 'a'.toInt() && ch <= 'z'.toInt()) nextChar()
                    val func = expr.substring(startPos, pos)
                    x = parseFactor()
                    x = when (func) {
                        "sqrt" -> sqrt(x)
                        "sin" -> sin(Math.toRadians(x))
                        "cos" -> cos(Math.toRadians(x))
                        "tan" -> tan(Math.toRadians(x))
                        "log" -> log10(x)
                        else -> throw RuntimeException("Unknown function: $func")
                    }
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }

                if (eat('^'.toInt())) x = x.pow(parseFactor()) // exponentiation

                return x
            }
        }.parse()
    }
}
