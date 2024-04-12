package com.example.homework5.network

import com.example.homework5.data.model.LoginModel
import com.example.homework5.data.model.User
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class RemoteApi {
    // URLs for profile and login endpoints
    private val PROFILE_URL = "https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/profile"
    private val LOGIN_URL = "https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/login"

    // Function to fetch profile data
    fun getProfile(userId: String, callback: (User?, String?) -> Unit) {
        val profileUrl = "$PROFILE_URL/$userId"
        Thread {
            val connection = URL(profileUrl).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.doInput = true

            try {
                // Read response from input stream
                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val response = StringBuilder()
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.forEachLine {
                        response.append(it.trim())
                    }
                    // Deserialize JSON response to com.example.denemee.data.model.User object using JSONObject
                    val jsonResponse = JSONObject(response.toString())

                    val user = User(
                        jsonResponse.getString("userId"),
                        jsonResponse.getString("fullName"),
                        jsonResponse.getString("email"),
                        jsonResponse.getString("password"),
                        jsonResponse.getString("phoneNumber"),
                        jsonResponse.getString("occupation"),
                        jsonResponse.getString("employer"),
                        jsonResponse.getString("country"),
                        jsonResponse.getString("latitude"),
                        jsonResponse.getString("longitude"),
                    )

                    callback(user, null) // Callback with user data and no error
                }
            } catch (e: Exception) {
                // Callback with no user data and error message
                callback(null, e.localizedMessage)
            } finally {
                // Disconnect HttpURLConnection
                connection.disconnect()
            }
        }.start()
    }

    // Function to perform login
    fun getLoginResponse(loginModel: LoginModel, callback: (String?, String?) -> Unit) {
        Thread {
            val connection = URL(LOGIN_URL).openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.doInput = true
            connection.doOutput = true // Enable output stream for sending data

            try {
                // Serialize LoginModel object to JSON using Gson
                val jsonUser = JSONObject().apply {
                    put("email", loginModel.email)
                    put("password", loginModel.password)
                }

                // Write JSON data to output stream
                val outputStreamWriter  = OutputStreamWriter(connection.outputStream)
                outputStreamWriter.write(jsonUser.toString())
                outputStreamWriter.flush()
                outputStreamWriter.close()

                // Read response from input stream
                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val response = StringBuilder()
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.forEachLine {
                        response.append(it.trim())
                    }
                    // Callback with login response and no error
                    callback(response.toString(), null)
                }
            } catch (e: Exception) {
                // Callback with no login response and error message
                callback(null, e.localizedMessage)
            } finally {
                // Disconnect HttpURLConnection
                connection.disconnect()
            }
        }.start()
    }
}