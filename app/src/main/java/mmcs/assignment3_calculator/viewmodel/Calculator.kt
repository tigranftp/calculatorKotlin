package mmcs.assignment3_calculator.viewmodel

import androidx.databinding.ObservableField

enum class Operation { ADD, SUB, MUL, DIV }

interface Calculator {

    // Observable display, should be referenced in layout
    var display: ObservableField<String>

    // Add new digit to current input and update display
    fun addDigit(dig: Int)

    // Add a floating point to current input and update display
    fun addPoint()

    // Add new operation into stack:
    //     perform a previously stored operation and update display
    //     start a new input
    fun addOperation(op: Operation)

    // Compute current operation and display a result
    fun compute()

    // Clear input
    fun clear()

    // Clear entire calculator state
    fun reset()
}