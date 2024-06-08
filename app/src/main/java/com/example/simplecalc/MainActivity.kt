package com.example.simplecalc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

import java.util.Stack
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.editText)
    }

    fun onNumberClick(view: android.view.View) {
        if (stateError) {
            editText.setText((view as Button).text)
            stateError = false
        } else {
            editText.append((view as Button).text)
        }
        lastNumeric = true
    }

    fun onOperatorClick(view: android.view.View) {
        if (lastNumeric && !stateError) {
            editText.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onClearClick(view: android.view.View) {
        editText.setText("")
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    fun onEqualClick(view: android.view.View) {
        if (lastNumeric && !stateError) {
            val text = editText.text.toString()
            try {
                val result = evaluate(text)
                editText.setText(result.toString())
                lastDot = true
            } catch (ex: Exception) {
                editText.setText("Error")
                stateError = true
                lastNumeric = false
            }
        }
    }

    fun onFunctionClick(view: android.view.View) {
        if (lastNumeric && !stateError) {
            val text = editText.text.toString()
            try {
                val result = when ((view as Button).text) {
                    "√" -> sqrt(text.toDouble())
                    "log" -> log10(text.toDouble())
                    "ln" -> ln(text.toDouble())
                    "sin" -> sin(Math.toRadians(text.toDouble()))
                    "cos" -> cos(Math.toRadians(text.toDouble()))
                    "tan" -> tan(Math.toRadians(text.toDouble()))
                    else -> throw IllegalArgumentException("Unknown function")
                }
                editText.setText(result.toString())
                lastNumeric = false
                lastDot = false
            } catch (ex: Exception) {
                editText.setText("Error")
                stateError = false
                lastNumeric = false
            }
        }
    }

    private fun evaluate(expression: String): Double {
        val tokens = expression.toCharArray()
        val values: Stack<Double> = Stack()
        val ops: Stack<Char> = Stack()
        var i = 0
        while (i < tokens.size) {
            if (tokens[i] == ' ') {
                i++
                continue
            }
            if (tokens[i] in '0'..'9' || tokens[i] == '.') {
                val sbuf = StringBuffer()
                while (i < tokens.size && (tokens[i] in '0'..'9' || tokens[i] == '.')) sbuf.append(tokens[i++])
                values.push(sbuf.toString().toDouble())
                i--
            } else if (tokens[i] == '(') {
                ops.push(tokens[i])
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(') values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                ops.pop()
            } else if (tokens[i] in listOf('+', '-', '*', '/', '^')) {
                while (ops.isNotEmpty() && hasPrecedence(tokens[i], ops.peek())) values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                ops.push(tokens[i])
            }
            i++
        }
        while (ops.isNotEmpty()) values.push(applyOp(ops.pop(), values.pop(), values.pop()))
        return values.pop()
    }

    private fun applyOp(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0.0) a / b else throw UnsupportedOperationException("Cannot divide by zero")
            '^' -> a.pow(b)
            else -> throw UnsupportedOperationException("Unknown operator")
        }
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false
        if (op1 == '^' && (op2 != '^')) return false
        return true
    }
}
