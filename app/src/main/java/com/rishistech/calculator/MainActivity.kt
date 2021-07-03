package com.rishistech.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

// import kotlinx.android.synthetic.main.activity_main.*
// for automatically Declare and initialize var of type widgets and
// it auto Binding of those widgets (findViewById(R.id.etWidgets))


// these val are for saving and retrieving the instance so that the value is stored if orientation is changed or the application is restarted or basically Operations to perform before the onStop() and after onStart()
private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {

    //Null can not be a value of a non-null type EditText
    //Property must be initialized or be abstract
    //lateinit this helps to initialize it late
//    private lateinit var result: EditText
//    private lateinit var newNumber: EditText

    //lazy defining a function which will be called to assign value to the property
    //function will be called once // parameters(LazyThreadSafetyMode.NONE)
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.tvOperation) }

    // Variables to hold the operands and type of calculation i.e Operation
    private var operand1: Double? = null//null// we need to record whether the value is null at the start.
    private var pendingOperation = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        etResult = findViewById(R.id.etResult)
//        etResult = findViewById(R.id.etNewNum)
//
//        // Data input buttons
//        val btn0: Button = findViewById(R.id.btn0)
//        val btn1: Button = findViewById(R.id.btn1)
//        val btn2: Button = findViewById(R.id.btn2)
//        val btn3: Button = findViewById(R.id.btn3)
//        val btn4: Button = findViewById(R.id.btn4)
//        val btn5: Button = findViewById(R.id.btn5)
//        val btn6: Button = findViewById(R.id.btn6)
//        val btn7: Button = findViewById(R.id.btn7)
//        val btn8: Button = findViewById(R.id.btn8)
//        val btn9: Button = findViewById(R.id.btn9)
//        val btnDot: Button = findViewById(R.id.btnDot)
//
//        // Operation buttons
//        val btnEquals = findViewById<Button>(R.id.btnEquals)
//        val btnDivide = findViewById<Button>(R.id.btnDivide)
//        val btnMultiply = findViewById<Button>(R.id.btnMultiply)
//        val btnMinus = findViewById<Button>(R.id.btnMinus)
//        val btnPlus = findViewById<Button>(R.id.btnPlus)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            etNewNum.append(b.text)
        }
        btn0.setOnClickListener(listener)
        btn1.setOnClickListener(listener)
        btn2.setOnClickListener(listener)
        btn3.setOnClickListener(listener)
        btn4.setOnClickListener(listener)
        btn5.setOnClickListener(listener)
        btn6.setOnClickListener(listener)
        btn7.setOnClickListener(listener)
        btn8.setOnClickListener(listener)
        btn9.setOnClickListener(listener)
        btnDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = etNewNum.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {// catch for handling . with operator error
                etNewNum.setText("")
            }
            pendingOperation = op
            tvOperation.text = pendingOperation
        }
        btnEquals.setOnClickListener(opListener)
        btnDivide.setOnClickListener(opListener)
        btnMultiply.setOnClickListener(opListener)
        btnMinus.setOnClickListener(opListener)
        btnPlus.setOnClickListener(opListener)

        btnNeg.setOnClickListener(View.OnClickListener {
            val value = etNewNum.text.toString()
            if (value.isEmpty()){
                etNewNum.setText("-")
            }else{
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    etNewNum.setText(doubleValue.toString())
                }catch (e: java.lang.NumberFormatException){
                    //etNewNum was "-" or ".", so clear it
                    etNewNum.setText("")
                }
            }
        })

        btnClear.setOnClickListener(View.OnClickListener {
            etNewNum.setText("")
            etResult.setText("")
            operand1 = null
            pendingOperation = "="
        })
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN //handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
                //!! bang bang operator
            }
        }
        etResult.setText(operand1.toString())
        etNewNum.setText("")
    }
    //https://stackoverflow.com/questions/44536114/whats-the-difference-between-and-in-kotlin
    //1. normal non-nullable type   (var a = 9)
    //2. Nullable type              (var a : String?= null)
    //a?.length safe call to the function (used in both)
    //a.length (only non-nullable)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1!=null){
            outState.putDouble(STATE_OPERAND1,operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED,true)
        }
        outState.putString(STATE_PENDING_OPERATION,pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if  (savedInstanceState.getBoolean(STATE_OPERAND1_STORED,false)){
            savedInstanceState.getDouble(STATE_OPERAND1)
        }else{
            null
        }

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION)!!
        tvOperation.text = pendingOperation
    }
}