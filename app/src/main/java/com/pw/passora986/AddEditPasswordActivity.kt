package com.pw.passora986

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.pw.passora986.databinding.ActivityAddEditPasswordBinding
import com.pw.passora986.db.PasswordViewModel
import com.pw.passora986.models.PasswordEntity
import kotlinx.coroutines.launch
import java.security.SecureRandom

class AddEditPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPasswordBinding
    // ViewModel
    private val viewModel: PasswordViewModel by viewModels()

    // Edit Mode
    private var passwordId = -1

    // Current Password
    private var currentPassword: PasswordEntity? = null

    // Categories
    private val categories = arrayOf(
        "Social",
        "Banking",
        "Shopping",
        "Email",
        "Work",
        "Entertainment",
        "Gaming",
        "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get password id
        passwordId = intent.getIntExtra(
            "password_id",
            -1
        )

        setupToolbar()

        setupSpinner()

        loadPassword()

        clickListeners()

    }
    /**
     * Toolbar
     */
    private fun setupToolbar() {

        binding.toolbar.setNavigationOnClickListener {

            finish()

        }

        if (passwordId != -1) {

            binding.toolbar.title = "Update Password"

            binding.btnSave.text = "Update Password"

        } else {

            binding.toolbar.title = "Add Password"

            binding.btnSave.text = "Save Password"

        }

    }

    /**
     * Spinner
     */
    private fun setupSpinner() {

        val adapter = ArrayAdapter(

            this,

            android.R.layout.simple_spinner_dropdown_item,

            categories

        )

        binding.spCategory.adapter = adapter

    }

    /**
     * Load Password
     */
    private fun loadPassword() {

        if (passwordId == -1)
            return

        lifecycleScope.launch {

            currentPassword =

                viewModel.getPasswordById(passwordId)

            currentPassword?.let {

                binding.edtWebsite.setText(it.website)

                binding.edtUsername.setText(it.username)

                binding.edtPassword.setText(it.password)

                binding.edtNotes.setText(it.notes)

                binding.cbFavorite.isChecked =
                    it.isFavorite

                val position =
                    categories.indexOf(it.category)

                if (position != -1) {

                    binding.spCategory.setSelection(
                        position
                    )

                }

            }

        }

    }

    /**
     * Click Listeners
     */
    private fun clickListeners() {

        binding.btnGenerate.setOnClickListener {

            binding.edtPassword.setText(

                generatePassword()

            )

        }

        binding.btnSave.setOnClickListener {

            savePassword()

        }

    }

    /**
     * Password Generator
     */
    private fun generatePassword(

        length: Int = 14

    ): String {

        val chars =

            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "@#\$%&*!"

        val random = SecureRandom()

        val builder = StringBuilder()

        repeat(length) {

            builder.append(

                chars[random.nextInt(chars.length)]

            )

        }

        return builder.toString()

    }

    /**
     * Validation
     */
    private fun validate(): Boolean {

        if (binding.edtWebsite.text.toString().trim().isEmpty()) {

            binding.edtWebsite.error = "Required"

            return false

        }

        if (binding.edtUsername.text.toString().trim().isEmpty()) {

            binding.edtUsername.error = "Required"

            return false

        }

        if (binding.edtPassword.text.toString().trim().isEmpty()) {

            binding.edtPassword.error = "Required"

            return false

        }

        return true

    }

    /**
     * Save / Update Password
     */
    private fun savePassword() {

        if (!validate())
            return

        val website = binding.edtWebsite.text.toString().trim()

        val username = binding.edtUsername.text.toString().trim()

        // Later replace with:
        // AESHelper.encrypt(binding.edtPassword.text.toString().trim())
        val password = binding.edtPassword.text.toString().trim()

        val notes = binding.edtNotes.text.toString().trim()

        val category = binding.spCategory.selectedItem.toString()

        val favorite = binding.cbFavorite.isChecked

        val currentTime = System.currentTimeMillis()

        if (passwordId == -1) {

            // Insert New Password

            val passwordEntity = PasswordEntity(

                website = website,

                username = username,

                password = password,

                notes = notes,

                category = category,

                isFavorite = favorite,

                createdAt = currentTime,

                updatedAt = currentTime

            )

            viewModel.insert(passwordEntity)

            Toast.makeText(
                this,
                "Password Saved",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            // Update Existing Password

            val updatedPassword = PasswordEntity(

                id = passwordId,

                website = website,

                username = username,

                password = password,

                notes = notes,

                category = category,

                isFavorite = favorite,

                createdAt = currentPassword?.createdAt ?: currentTime,

                updatedAt = currentTime

            )

            viewModel.update(updatedPassword)

            Toast.makeText(
                this,
                "Password Updated",
                Toast.LENGTH_SHORT
            ).show()

        }

        finish()

    }

}