package com.example.simplecalc

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private var currentInput = ""
    private var operator = ""
    private var firstOperand = 0.0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)

        val buttons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonDot, R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply,
            R.id.buttonDivide, R.id.buttonEqual
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                onButtonClick(it as Button)
            }
        }
    }

    private fun onButtonClick(button: Button) {
        val buttonText = button.text.toString()

        when (buttonText) {
            "+", "-", "*", "/" -> {
                operator = buttonText
                firstOperand = currentInput.toDouble()
                currentInput = ""
            }
            "=" -> {
                val secondOperand = currentInput.toDouble()
                val result = when (operator) {
                    "+" -> firstOperand + secondOperand
                    "-" -> firstOperand - secondOperand
                    "*" -> firstOperand * secondOperand
                    "/" -> firstOperand / secondOperand
                    else -> 0.0
                }
                input.setText(result.toString())
                currentInput = result.toString()
                operator = ""
            }
            else -> {
                currentInput += buttonText
                input.setText(currentInput)
            }
        }
    }
}
