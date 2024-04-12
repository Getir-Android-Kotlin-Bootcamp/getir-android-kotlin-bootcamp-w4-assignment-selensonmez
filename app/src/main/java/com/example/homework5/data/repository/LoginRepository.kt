package com.example.homework5.data.repository
import com.example.homework5.network.RemoteApi
import com.example.homework5.data.model.LoginModel

class LoginRepository (private val remoteApi: RemoteApi) {

    fun getLoginInfo(loginModel: LoginModel, callback: (String?, String?) -> Unit) {
        remoteApi.getLoginResponse(loginModel) { response, errorMsg ->
            if (response != null) {
                callback(response, null)
            } else {
                callback(null, errorMsg)
            }
        }
    }
}
