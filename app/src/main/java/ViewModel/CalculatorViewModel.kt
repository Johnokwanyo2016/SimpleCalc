package com.example.simplecalc.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    val input = MutableLiveData<String>("")
    private var operator = ""
    private var firstOperand = 0.0

    fun onNumberClick(number: String) {
        input.value += number
    }

    fun onOperatorClick(op: String) {
        operator = op
        firstOperand = input.value?.toDoubleOrNull() ?: 0.0
        input.value = ""
    }

    fun onEqualClick() {
        val secondOperand = input.value?.toDoubleOrNull() ?: 0.0
        val result = when (operator) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "*" -> firstOperand * secondOperand
            "/" -> firstOperand / secondOperand
            else -> 0.0
        }
        input.value = result.toString()
    }

    fun onClearClick() {
        input.value = ""
        operator = ""
        firstOperand = 0.0
    }
}
