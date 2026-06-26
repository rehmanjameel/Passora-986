package com.pw.passora986.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pw.passora986.models.PasswordEntity

/**
 * DAO class for Password CRUD operations
 */
@Dao
interface PasswordDao {

    // Get all passwords
    @Query("SELECT * FROM password_table ORDER BY website ASC")
    fun getAllPasswords(): LiveData<List<PasswordEntity>>

    // Search Password
    @Query("""
        SELECT * FROM password_table
        WHERE website LIKE '%' || :query || '%'
        OR username LIKE '%' || :query || '%'
        ORDER BY website ASC
    """)
    fun searchPasswords(query: String): LiveData<List<PasswordEntity>>

    // Favorite Passwords
    @Query("SELECT * FROM password_table WHERE isFavorite = 1")
    fun getFavorites(): LiveData<List<PasswordEntity>>

    // Get Single Password
    @Query("SELECT * FROM password_table WHERE id=:id")
    suspend fun getPasswordById(id: Int): PasswordEntity?

    // Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(password: PasswordEntity)

    // Update
    @Update
    suspend fun update(password: PasswordEntity)

    // Delete
    @Delete
    suspend fun delete(password: PasswordEntity)

    // Delete All
    @Query("DELETE FROM password_table")
    suspend fun deleteAll()

}