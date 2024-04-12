package com.example.homework5

import com.example.homework5.network.RemoteApi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.homework5.data.model.LoginModel
import com.example.homework5.databinding.ActivityMainBinding
import com.example.homework5.presentation.viewmodel.LoginViewModel
import com.example.homework5.presentation.viewmodel.ProfileViewModel
import com.example.homework5.data.repository.LoginRepository
import com.example.homework5.data.repository.ProfileRepository

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityMainBinding
    private val remoteApi = RemoteApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Call the initializeViews() method
        initializeViews()
        // Call the observeLoginResponse() method
        observeLoginResponse()
    }

    // Initialize the views
    private fun initializeViews() {
        // Set the content view using DataBindingUtil
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Set the lifecycle owner
        binding.lifecycleOwner = this
        // Initialize the ViewModels
        initializeViewModels()
        // Set the ViewModel for the binding
        binding.viewModel = viewModel
    }

    // Initialize the ViewModels
    private fun initializeViewModels() {
        // Create instances of ProfileRepository and ProfileViewModel
        val profileRepository = ProfileRepository(remoteApi)
        viewModel = ProfileViewModel(profileRepository)

        // Create instances of LoginRepository and LoginViewModel
        val loginRepository = LoginRepository(remoteApi)
        loginViewModel = LoginViewModel(loginRepository)
    }

    // Observe the login response
    private fun observeLoginResponse() {
        // Create a user object
        val user = LoginModel("harunkor@gmail.com", "Hk123123")
        // Call the login method of loginViewModel
        loginViewModel.login(user)

        // Observe the login response
        loginViewModel.loginResponse.observe(this) { response ->
            response?.let {
                displayLoginResponse(response)
                fetchUserProfile(response)
            }
        }

        // Observe errors
        loginViewModel.error.observe(this) { error ->
            error?.let {
                Log.v("selen", error.toString())
            }
        }
    }

    // Display the login response
    private fun displayLoginResponse(response: String) {
        binding.textViewLoginData.text = "userID: $response"
    }

    // Fetch user profile
    private fun fetchUserProfile(response: String) {
        viewModel.fetchProfile(response)
        // Observe userData
        viewModel.userData.observe(this) { user ->
            user?.let {
                binding.textViewId.text = "ID: ${user.id}"
                binding.textViewName.text = "Name: ${user.fullName}"
                binding.textViewEmail.text = "Email: ${user.email}"
                binding.textViewCountry.text = "Country: ${user.country}"
                binding.textViewEmployer.text = "Employer: ${user.employer}"
                binding.textViewLatitude.text = "Latitude: ${user.latitude}"
                binding.textViewLongitude.text = "Longitude: ${user.longitude}"
                binding.textViewOccupation.text = "Occupation: ${user.occupation}"
                binding.textViewPhone.text = "Phone Number: ${user.phoneNumber}"
            }
        }
    }
}