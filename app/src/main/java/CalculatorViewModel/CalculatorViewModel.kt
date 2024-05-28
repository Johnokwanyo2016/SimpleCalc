package CalculatorViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {
    // LiveData to hold the input expression
    val input: MutableLiveData<String> = MutableLiveData("")

    // Method to handle number button clicks
    fun onNumberClick(number: String) {
        // Append the clicked number to the current input value
        input.value = input.value + number
    }

    // Method to handle operator button clicks
    fun onOperatorClick(operator: String) {
        // Append the clicked operator to the current input value with spaces
        input.value = input.value + " $operator "
    }

    // Method to handle clear button click
    fun onClearClick() {
        // Clear the input value
        input.value = ""
    }

    // Method to handle equal button click
    fun onEqualClick() {
        try {
            // Evaluate the current input expression
            val result = eval(input.value ?: "")
            // Update the input with the result
            input.value = result.toString()
        } catch (e: Exception) {
            // Set the input to "Error" in case of any exception
            input.value = "Error"
        }
    }

    // Private method to evaluate the mathematical expression
    private fun eval(expression: String): Double {
        if (expression.trim().isEmpty()) {
            throw IllegalArgumentException("Empty expression")
        }

        // Split the expression by spaces to get the tokens
        val tokens = expression.split(" ")
        if (tokens.size != 3) {
            throw IllegalArgumentException("Invalid expression")
        }

        // Parse the left operand
        val left = tokens[0].toDouble()
        // Get the operator
        val operator = tokens[1]
        // Parse the right operand
        val right = tokens[2].toDouble()

        // Perform the operation based on the operator
        return when (operator) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> left / right
            else -> throw IllegalArgumentException("Unknown operator")
        }
    }
}
