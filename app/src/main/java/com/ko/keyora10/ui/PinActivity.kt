package com.ko.keyora10.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ko.keyora10.R
import com.ko.keyora10.databinding.ActivityPinBinding
import com.ko.keyora10.utils.AppPreferences
import com.ko.keyora10.utils.HashUtils
import com.ko.keyora10.utils.PreferenceKeys

class PinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinBinding

    private val enteredPin = StringBuilder()

    private var firstPin = ""

    private var isConfirmStep = false

    companion object {

        const val EXTRA_MODE = "extra_mode"

        const val MODE_CREATE = 0

        const val MODE_CHANGE = 1

    }
    private var mode = MODE_CREATE

    private var step = 0

    private var oldPin = ""


    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // set the pin mode
        mode = intent.getIntExtra(
            EXTRA_MODE,
            MODE_CREATE
        )

        updateScreen()

        initClicks()

    }

    private fun updateScreen() {

        when (mode) {

            MODE_CREATE -> {

                when (step) {

                    0 -> {

                        binding.txtTitle.text = "Create Security PIN"

                        binding.txtSubtitle.text =
                            "Create a 4-digit PIN to protect your passwords."

                    }

                    1 -> {

                        binding.txtTitle.text = "Confirm PIN"

                        binding.txtSubtitle.text =
                            "Enter the same PIN again."

                    }

                }

            }

            MODE_CHANGE -> {

                when (step) {

                    0 -> {

                        binding.txtTitle.text = "Current PIN"

                        binding.txtSubtitle.text =
                            "Enter your current PIN."

                    }

                    1 -> {

                        binding.txtTitle.text = "New PIN"

                        binding.txtSubtitle.text =
                            "Create a new PIN."

                    }

                    2 -> {

                        binding.txtTitle.text = "Confirm PIN"

                        binding.txtSubtitle.text =
                            "Confirm your new PIN."

                    }

                }

            }

        }

    }

    private fun initClicks() {

        binding.btn0.setOnClickListener { addDigit("0") }
        binding.btn1.setOnClickListener { addDigit("1") }
        binding.btn2.setOnClickListener { addDigit("2") }
        binding.btn3.setOnClickListener { addDigit("3") }
        binding.btn4.setOnClickListener { addDigit("4") }
        binding.btn5.setOnClickListener { addDigit("5") }
        binding.btn6.setOnClickListener { addDigit("6") }
        binding.btn7.setOnClickListener { addDigit("7") }
        binding.btn8.setOnClickListener { addDigit("8") }
        binding.btn9.setOnClickListener { addDigit("9") }

        binding.btnDelete.setOnClickListener {

            if (enteredPin.isNotEmpty()) {

                enteredPin.deleteCharAt(
                    enteredPin.length - 1
                )

                updateIndicators()

            }

        }

        binding.btnDone.setOnClickListener {

            if (enteredPin.length != 4) {

                Toast.makeText(
                    this,
                    "Enter 4 digit PIN",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener

            }

            when (mode) {

                MODE_CREATE -> {

                    handleCreatePin()

                }

                MODE_CHANGE -> {

                    handleChangePin()

                }

            }



        }

    }

    private fun handleCreatePin() {

        if (step == 0) {

            firstPin = enteredPin.toString()

            enteredPin.clear()

            updateIndicators()

            step = 1

            updateScreen()

            return

        }

        if (firstPin == enteredPin.toString()) {

            AppPreferences.saveString(
                PreferenceKeys.USER_PIN,
                HashUtils.sha256(firstPin)
            )

            Toast.makeText(
                this,
                "PIN Created",
                Toast.LENGTH_SHORT
            ).show()

            setResult(RESULT_OK)

            finish()

        } else {

            Toast.makeText(
                this,
                "PIN doesn't match",
                Toast.LENGTH_SHORT
            ).show()

            firstPin = ""

            step = 0

            enteredPin.clear()

            updateIndicators()

            updateScreen()

        }

    }

    private fun handleChangePin() {

        when (step) {

            0 -> {

                val savedHash = AppPreferences.getString(
                    PreferenceKeys.USER_PIN
                )

                if (HashUtils.sha256(
                        enteredPin.toString()
                    ) != savedHash
                ) {

                    Toast.makeText(
                        this,
                        "Wrong PIN",
                        Toast.LENGTH_SHORT
                    ).show()

                    enteredPin.clear()

                    updateIndicators()

                    return

                }

                oldPin = enteredPin.toString()

                enteredPin.clear()

                updateIndicators()

                step = 1

                updateScreen()

            }

            1 -> {

                firstPin = enteredPin.toString()

                enteredPin.clear()

                updateIndicators()

                step = 2

                updateScreen()

            }

            2 -> {

                if (firstPin != enteredPin.toString()) {

                    Toast.makeText(
                        this,
                        "PIN doesn't match",
                        Toast.LENGTH_SHORT
                    ).show()

                    enteredPin.clear()

                    updateIndicators()

                    step = 1

                    updateScreen()

                    return

                }

                AppPreferences.saveString(
                    PreferenceKeys.USER_PIN,
                    HashUtils.sha256(firstPin)
                )

                Toast.makeText(
                    this,
                    "PIN Updated",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            }

        }

    }

    private fun addDigit(number: String) {

        if (enteredPin.length == 4)
            return

        enteredPin.append(number)

        updateIndicators()

    }

    private fun updateIndicators() {

        val views = listOf(

            binding.pin1,
            binding.pin2,
            binding.pin3,
            binding.pin4

        )

        for (i in views.indices) {

            if (i < enteredPin.length) {

                views[i].setBackgroundResource(
                    R.drawable.bg_pin_filled
                )

            } else {

                views[i].setBackgroundResource(
                    R.drawable.bg_pin_empty
                )

            }

        }

    }
}