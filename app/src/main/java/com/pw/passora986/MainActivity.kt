package com.pw.passora986

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pw.passora986.adapter.PasswordAdapter
import com.pw.passora986.databinding.ActivityMainBinding
import com.pw.passora986.db.PasswordViewModel
import com.pw.passora986.models.PasswordEntity
import com.pw.passora986.ui.SettingsActivity
import com.pw.passora986.utils.AppPreferences
import com.pw.passora986.utils.HashUtils
import com.pw.passora986.utils.PreferenceKeys

class MainActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivityMainBinding

    // Adapter
    private lateinit var adapter: PasswordAdapter

    // ViewModel
    private val viewModel: PasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecycler()

        observeData()

        searchPasswords()

        swipeToDelete()

        clickListeners()
    }

    /**
     * RecyclerView
     */
    private fun initRecycler() {

        adapter = PasswordAdapter(

            onViewClick = { password ->

                verifyPinAndShowPassword(password)

            },

            onCopyClick = { password ->

                verifyPinAndCopyPassword(password)

            },

            onEditClick = { password ->

                verifyPinAndEditPassword(password)

            }

        )

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerView.adapter = adapter

    }

    private fun verifyPinAndShowPassword(
        password: PasswordEntity
    ) {

        showPinDialog {

            MaterialAlertDialogBuilder(this)
                .setTitle(password.website)
                .setMessage(password.password)
                .setPositiveButton("Close", null)
                .show()

        }

    }

    private fun verifyPinAndCopyPassword(
        password: PasswordEntity
    ) {

        showPinDialog {

            val clipboard =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(
                "Password",
                password.password // Later AESHelper.decrypt(...)
            )

            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                this,
                "Password Copied",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    private fun verifyPinAndEditPassword(
        password: PasswordEntity
    ) {

        showPinDialog {

            val intent = Intent(
                this,
                AddEditPasswordActivity::class.java
            )

            intent.putExtra(
                "password_id",
                password.id
            )

            startActivity(intent)

        }

    }


    /**
     * Observe LiveData
     */
    private fun observeData() {

        viewModel.allPasswords.observe(this) {

            adapter.submitList(it)

            binding.txtEmpty.visibility =
                if (it.isEmpty()) android.view.View.VISIBLE
                else android.view.View.GONE

        }

    }

    /**
     * Search Password
     */
    private fun searchPasswords() {

        binding.searchView.setOnQueryTextListener(

            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false

                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    viewModel.searchPasswords(
                        newText ?: ""
                    ).observe(this@MainActivity) {

                        adapter.submitList(it)

                    }

                    return true

                }

            }

        )

    }

    /**
     * Click Listeners
     */
    private fun clickListeners() {

        binding.fab.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    AddEditPasswordActivity::class.java
                )

            )

        }

        binding.btnSettings.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    SettingsActivity::class.java
                )

            )

        }

    }

    /**
     * Swipe Delete
     */
    private fun swipeToDelete() {

        val callback = object : ItemTouchHelper.SimpleCallback(

            0,

            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                return false

            }

            override fun onSwiped(

                viewHolder: RecyclerView.ViewHolder,

                direction: Int

            ) {

                val password =

                    adapter.currentList[
                        viewHolder.adapterPosition
                    ]

                showDeleteDialog(password)

            }

        }

        ItemTouchHelper(callback)

            .attachToRecyclerView(

                binding.recyclerView

            )

    }

    /**
     * Delete Dialog
     */
    private fun showDeleteDialog(

        password: PasswordEntity

    ) {

        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Password")
            .setMessage("Are you sure you want to delete this password?")
            .setPositiveButton("Delete") { _, _ ->

                viewModel.delete(password)

            }
            .setNegativeButton("Cancel") { _, position ->

                adapter.notifyItemChanged(position)

            }
            .show()

    }

    private fun showPinDialog(
        onSuccess: () -> Unit
    ) {

        val builder = MaterialAlertDialogBuilder(this)

        val view = layoutInflater.inflate(
            R.layout.dialog_verify_pin,
            null
        )

        builder.setView(view)

        val dialog = builder.create()

        dialog.setCancelable(false)

        val edtPin =
            view.findViewById<EditText>(R.id.edtPin)

        val btnCancel =
            view.findViewById<Button>(R.id.btnCancel)

        val btnVerify =
            view.findViewById<Button>(R.id.btnVerify)

        btnCancel.setOnClickListener {

            dialog.dismiss()

        }

        btnVerify.setOnClickListener {

            val pin = edtPin.text.toString()

            if (pin.length != 4) {

                edtPin.error = "Enter PIN"

                return@setOnClickListener

            }

            val savedPin = AppPreferences.getString(
                PreferenceKeys.USER_PIN
            )

            if (HashUtils.sha256(pin) == savedPin) {

                dialog.dismiss()

                onSuccess.invoke()

            } else {

                edtPin.error = "Wrong PIN"

            }

        }

        dialog.show()

    }

}