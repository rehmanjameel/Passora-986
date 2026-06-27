package com.pw.passora986.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pw.passora986.R
import com.pw.passora986.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()

        setVersion()

        clickListeners()

    }

    /**
     * Toolbar
     */
    private fun setupToolbar() {

        binding.toolbar.setNavigationOnClickListener {

            finish()

        }

    }

    /**
     * App Version
     */
    private fun setVersion() {

        try {

            val versionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                ).versionName

            } else {

                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(
                    packageName,
                    0
                ).versionName

            }

            binding.txtVersion.text = "Version $versionName"

        } catch (e: Exception) {

            binding.txtVersion.text = "Version 1.0"

        }

    }

    /**
     * Click Listeners
     */
    private fun clickListeners() {

        // Change PIN
        binding.cardChangePin.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    PinActivity::class.java
                ).apply {

                    putExtra(
                        PinActivity.EXTRA_MODE,
                        PinActivity.MODE_CHANGE
                    )

                }

            )

        }

        // Privacy Policy
        binding.cardPrivacy.setOnClickListener {

            val url = ""

            startActivity(

                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )

            )

        }

        // Share App
        binding.cardShare.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)

            shareIntent.type = "text/plain"

            shareIntent.putExtra(

                Intent.EXTRA_SUBJECT,

                "Passora986"

            )

            shareIntent.putExtra(

                Intent.EXTRA_TEXT,

                "Check out Passora986 Password Manager.\n\n" +
                        "https://play.google.com/store/apps/details?id=$packageName"

            )

            startActivity(

                Intent.createChooser(

                    shareIntent,

                    "Share App"

                )

            )

        }

        // Rate App
        binding.cardRate.setOnClickListener {

            try {

                startActivity(

                    Intent(

                        Intent.ACTION_VIEW,

                        Uri.parse(

                            "market://details?id=$packageName"

                        )

                    )

                )

            } catch (e: Exception) {

                startActivity(

                    Intent(

                        Intent.ACTION_VIEW,

                        Uri.parse(

                            "https://play.google.com/store/apps/details?id=$packageName"

                        )

                    )

                )

            }

        }

        // About
        binding.cardAbout.setOnClickListener {

            showAboutDialog()

        }

    }

    /**
     * About Dialog
     */
    private fun showAboutDialog() {

        AlertDialog.Builder(this)

            .setTitle("Passora986")

            .setMessage(

                "Passora986 is a secure offline password manager.\n\n" +

                        "• Store passwords locally\n" +

                        "• PIN Protected\n" +

                        "• Fast & Lightweight\n" +

                        "• No Internet Required\n\n" +

                        "Version 1.0"

            )

            .setPositiveButton("OK", null)

            .show()

    }
}