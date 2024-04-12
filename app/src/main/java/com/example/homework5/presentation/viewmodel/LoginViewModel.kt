package com.example.homework5.presentation.viewmodel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework5.data.model.LoginModel
import com.example.homework5.data.repository.LoginRepository

// Define LoginViewModel class which extends ViewModel
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    // MutableLiveData for holding login response data
    val loginResponse = MutableLiveData<String?>()

    // MutableLiveData for holding error messages
    val error = MutableLiveData<String?>()

    // Method to perform login
    fun login(loginModel: LoginModel) {
        // Call the login method of the loginRepository
        loginRepository.getLoginInfo(loginModel) { response, errorMsg ->
            if (response != null) {
                // Post the response to loginResponse MutableLiveData
                loginResponse.postValue(response)
            } else {
                // Post the error message to error MutableLiveData
                error.postValue(errorMsg)
            }
        }
    }
}
