package com.pw.passora986.db


import androidx.lifecycle.LiveData
import com.pw.passora986.models.PasswordEntity

/**
 * Repository class
 * Acts as a bridge between Room Database and ViewModel
 */
class PasswordRepository(
    private val passwordDao: PasswordDao
) {

    // Get all passwords
    val allPasswords: LiveData<List<PasswordEntity>> =
        passwordDao.getAllPasswords()

    // Search passwords
    fun searchPasswords(query: String): LiveData<List<PasswordEntity>> {
        return passwordDao.searchPasswords(query)
    }

    // Get favorite passwords
    fun getFavorites(): LiveData<List<PasswordEntity>> {
        return passwordDao.getFavorites()
    }

    // Get password by id
    suspend fun getPasswordById(id: Int): PasswordEntity? {
        return passwordDao.getPasswordById(id)
    }

    // Insert
    suspend fun insert(password: PasswordEntity) {
        passwordDao.insert(password)
    }

    // Update
    suspend fun update(password: PasswordEntity) {
        passwordDao.update(password)
    }

    // Delete
    suspend fun delete(password: PasswordEntity) {
        passwordDao.delete(password)
    }

    // Delete All
    suspend fun deleteAll() {
        passwordDao.deleteAll()
    }

}