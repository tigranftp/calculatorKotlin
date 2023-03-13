package mmcs.assignment3_calculator.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

class CalculatorViewModel : BaseObservable(), Calculator {
    override var display = ObservableField<String>()

    private var numberHavePoint = false

    override fun addDigit(dig: Int) {
        if (display.get() != null) {
            val number = display.get() + dig
            display.set(number)
            return
        }

        display.set(dig.toString())
    }

    override fun addPoint() {
        if (display.get() == "" || display.get() == null || numberHavePoint) {
            return
        }
        numberHavePoint = true
        display.set("${display.get()}.")
    }

    override fun addOperation(op: Operation) {

        if (display.get() == "" || display.get() == null || numberHavePoint) {
            if (op == Operation.SUB) {
                display.set("-")
            }
            return
        }

        var operation = ""

        var lastCharacter = "${display.get()}".last()

        when (lastCharacter) {
            '+', '×', '÷','-'  -> display.set("${display.get()}".dropLast(1))
        }

        when (op) {
            Operation.ADD -> operation = "+"
            Operation.SUB -> operation = "-"
            Operation.MUL -> operation = "×"
            Operation.DIV -> operation = "÷"
        }
        numberHavePoint = false
        println("${display.get()}$operation")
        display.set("${display.get()}$operation")
    }

    override fun compute() {

        println("${display.get()}")

        if (display.get() == null || display.get() == "") {
            return
        }

        if ("${display.get()}".last() == '!') {
            display.set("")
            return
        }

        numberHavePoint = false

        var result = evaluate("${display.get()}")

        if ((result.toInt() - result) != 0.0) {
            display.set(((result * 100000).toInt() / 100000.0).toString())
            return
        }

        display.set(result.toInt().toString())

    }

    fun evaluate(str: String): Double {
        data class Data(val rest: List<Char>, val value: Double)

        return object : Any() {

            fun parse(chars: List<Char>): Double {
                return getExpression(chars.filter { it != ' ' })
                    .also { if (it.rest.isNotEmpty()) throw RuntimeException("Unexpected character: ${it.rest.first()}") }
                    .value
            }

            private fun getExpression(chars: List<Char>): Data {
                var (rest, carry) = getTerm(chars)
                while (true) {
                    when {
                        rest.firstOrNull() == '+' -> rest =
                            getTerm(rest.drop(1)).also { carry += it.value }.rest
                        rest.firstOrNull() == '-' -> rest =
                            getTerm(rest.drop(1)).also { carry -= it.value }.rest
                        else -> return Data(rest, carry)
                    }
                }
            }

            fun getTerm(chars: List<Char>): Data {
                var (rest, carry) = getFactor(chars)
                while (true) {
                    when {
                        rest.firstOrNull() == '×' -> rest =
                            getTerm(rest.drop(1)).also { carry *= it.value }.rest
                        rest.firstOrNull() == '÷' -> {
                            rest = getTerm(rest.drop(1)).also {
//                                if (it.value == 0.0)
//                                {
//                                    throw Exception("Division by zero!")
//                                }
                                carry /= it.value
                            }.rest
                        }
                        else -> return Data(rest, carry)
                    }
                }
            }

            fun getFactor(chars: List<Char>): Data {
                return when (val char = chars.firstOrNull()) {
                    '+' -> getFactor(chars.drop(1)).let { Data(it.rest, +it.value) }
                    '-' -> getFactor(chars.drop(1)).let { Data(it.rest, -it.value) }
                    '(' -> getParenthesizedExpression(chars.drop(1))
                    in '0'..'9', ',' -> getNumber(chars)
                    else -> throw RuntimeException("Unexpected character: $char")
                }
            }

            fun getParenthesizedExpression(chars: List<Char>): Data {
                return getExpression(chars)
                    .also { if (it.rest.firstOrNull() != ')') throw RuntimeException("Missing closing parenthesis") }
                    .let { Data(it.rest.drop(1), it.value) }
            }

            fun getNumber(chars: List<Char>): Data {
                val s = chars.takeWhile { it.isDigit() || it == '.' }.joinToString("")
                return Data(chars.drop(s.length), s.toDouble())
            }

        }.parse(str.toList())

    }

    override fun clear() {
        if (display.get() == null || display.get() == "") {
            return
        }


        if ("${display.get()}".last() == '.') {
            numberHavePoint = false
        }
        println("${display.get()}".dropLast(1))

        var text = "${display.get()}".dropLast(1)
        when (text) {
            "+", "-", "÷", "×" -> {
                display.set("")
                return
            }
        }

        display.set("${display.get()}".dropLast(1))
    }

    override fun reset() {
        display.set("")
        numberHavePoint = false
    }
}