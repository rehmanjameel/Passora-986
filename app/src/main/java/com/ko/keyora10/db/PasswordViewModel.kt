package com.ko.keyora10.db


import android.app.Application
import androidx.lifecycle.*
import com.ko.keyora10.models.PasswordEntity
import kotlinx.coroutines.launch

/**
 * ViewModel class
 */
class PasswordViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: PasswordRepository

    // Live list of passwords
    val allPasswords: LiveData<List<PasswordEntity>>

    init {

        val dao = PasswordDatabase
            .getDatabase(application)
            .passwordDao()

        repository = PasswordRepository(dao)

        allPasswords = repository.allPasswords

    }

    // Search
    fun searchPasswords(query: String): LiveData<List<PasswordEntity>> {
        return repository.searchPasswords(query)
    }

    // Favorites
    fun getFavorites(): LiveData<List<PasswordEntity>> {
        return repository.getFavorites()
    }

    // Get Password by ID
    suspend fun getPasswordById(id: Int): PasswordEntity? {
        return repository.getPasswordById(id)
    }

    // Insert
    fun insert(password: PasswordEntity) {

        viewModelScope.launch {

            repository.insert(password)

        }

    }

    // Update
    fun update(password: PasswordEntity) {

        viewModelScope.launch {

            repository.update(password)

        }

    }

    // Delete
    fun delete(password: PasswordEntity) {

        viewModelScope.launch {

            repository.delete(password)

        }

    }

    // Delete All
    fun deleteAll() {

        viewModelScope.launch {

            repository.deleteAll()

        }

    }

}