package com.pw.passora986.models


import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class for storing passwords.
 * Password value should be encrypted before saving.
 */
@Entity(tableName = "password_table")
data class PasswordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Website/App Name
    val website: String,

    // Username / Email
    val username: String,

    // Encrypted Password
    val password: String,

    // Optional Notes
    val notes: String,

    // Category (Social, Banking, Shopping...)
    val category: String,

    // Favorite
    val isFavorite: Boolean = false,

    // Creation Time
    val createdAt: Long = System.currentTimeMillis(),

    // Update Time
    val updatedAt: Long = System.currentTimeMillis()

)
