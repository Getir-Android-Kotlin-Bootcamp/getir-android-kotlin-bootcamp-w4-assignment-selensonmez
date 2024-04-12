package com.example.homework5.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework5.data.model.User
import com.example.homework5.data.repository.ProfileRepository

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    // MutableLiveData for holding user data
    private val _userData = MutableLiveData<User?>()
    val userData: MutableLiveData<User?> = _userData

    // MutableLiveData for holding error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Method to fetch user profile
    fun fetchProfile(userId: String) {
        // Call the getProfile method of the profileRepository
        profileRepository.getProfile(userId) { user, error ->
            if (user != null) {
                // Post the user data to _userData MutableLiveData
                userData.postValue(user)
            } else {
                // Post the error message to _error MutableLiveData, or "Unknown error" if error is null
                _error.postValue(error ?: "Unknown error")
            }
        }
    }
}


