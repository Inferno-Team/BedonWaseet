package com.inferno.mobile.bedon_waseet.ui.splash.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inferno.mobile.bedon_waseet.repos.SignUpRepo
import com.inferno.mobile.bedon_waseet.responses.LoginResponse
import com.inferno.mobile.bedon_waseet.utils.UserType
import java.io.File

class SignUpViewModel : ViewModel() {
    private var repo = SignUpRepo

    fun register(username: String,
                 password: String,
                 email: String,
                 type: UserType,
                 phone: String,
                 file: File?):LiveData<LoginResponse>{
        return repo.register(username, password, email,phone, type, file)
    }
}
