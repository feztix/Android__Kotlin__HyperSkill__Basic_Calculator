package org.hyperskill.calculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var opCode = 0
        var equalPressed = false
        var input : String

        val numStack = mutableListOf<Double>()
        // val opStack = mutableListOf<String>()
        val edit = findViewById<EditText>(R.id.editText)
        val btnClear = findViewById<Button>(R.id.clearButton)
        val btnDot = findViewById<Button>(R.id.dotButton)

        val operation = mapOf(
            11 to { a: Double, b: Double -> a + b },
            12 to { a: Double, b: Double -> a - b },
            13 to { a: Double, b: Double -> a * b },
            14 to { a: Double, b: Double -> a / b }
        )

        val numbers = arrayOf<Button>(
            findViewById(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9),
            btnDot,
            findViewById(R.id.addButton),
            findViewById(R.id.subtractButton),
            findViewById(R.id.multiplyButton),
            findViewById(R.id.divideButton),
            findViewById(R.id.equalButton)
        )

        numbers.forEachIndexed { i, v ->
            v.setOnClickListener {
                when {

                    // editText contains zero only, new input is zero
                    i == 0 && edit.text.toString() == "0" -> {
                        edit.setText("0")
                    }

//                    // editText contains zero only, new input is zero
//                    i == 0 && edit.text.toString() == "0${1..9}" -> {
//                        edit.setText("${edit.text}")
//                    }

                    // editText contains decimal point, new input is decimal point
                    i == 10 && edit.text.toString().isEmpty() -> {
                        edit.setText("0.")
                    }

                    // editText contains decimal point, new input is decimal point
                    i == 10 && edit.text.toString().contains(".") -> {
                    }

                    // editText does not contain decimal point, new input is decimal point
                    i == 10 -> edit.setText("${edit.text}.")

                    // digit button pressed
                    i in 0..9 -> {
                        if (equalPressed && edit.text.toString() != "-") {
                            edit.setText("")
                            input = ""
                            equalPressed = false
                        }
                        edit.setText("${edit.text}$i")
                    }

                    // plus, subtract, multiplication, division buttons pressed.
                    i in 11..14 -> {
                        when {
                            // starting a negative number
                            edit.text.isEmpty() -> if (i % 10 == 2) edit.setText("-")
                            else -> {
                                input = edit.text.toString()
                                numStack.add(input.toDouble())
                                edit.setText("")
                                edit.hint = input
                                opCode = i
                            }
                        }
                    }

                    // equal button pressed.
                    i == 15 -> {
                        if (opCode == 0)
                            return@setOnClickListener

                        numStack.add(editText.text.toString().toDouble())
                        val b = numStack.removeLast()
                        val a = numStack.removeLast()
                        input = operation[opCode]!!.invoke(a, b).toString()
                        numStack.add(input.toDouble())

                        Log.i("Result after pressing =", "onCreate: a: $a, b: $b, input: $input")

                        opCode = 0
                        if (input.substringAfter(".").toInt() == 0)
                            input = input.substringBefore(".")
                        edit.setText(input)
                        equalPressed = true
                    }
                }
            }
        }

        btnClear.setOnClickListener {
            edit.setText("")
            edit.hint = "0"
            input = ""
        }
    }
}