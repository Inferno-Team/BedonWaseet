package com.inferno.mobile.bedon_waseet.repos

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.responses.LoginResponse
import com.inferno.mobile.bedon_waseet.utils.UserType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object SignUpRepo {
    private const val TAG: String = "SignUpRepo"

    fun register(
        username: String,
        password: String,
        email: String,
        phone: String,
        type: UserType,
        file: File?
    ): MutableLiveData<LoginResponse> {
        val liveData = MutableLiveData<LoginResponse>()
        val nameBody = username.toRequestBody("text/plan".toMediaTypeOrNull())
        val passwordBody = password.toRequestBody("text/plan".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plan".toMediaTypeOrNull())
        val phoneBody = phone.toRequestBody("text/plan".toMediaTypeOrNull())
        val typeBody = type.toString().toRequestBody("text/plan".toMediaTypeOrNull())
        val call = if (file != null) {
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("avatar", file.name, requestBody)
            BaseApplication.service.registerWithAvatar(
                nameBody, emailBody,
                passwordBody, typeBody, phoneBody, part
            )
        }else{
            BaseApplication.service.registerWithoutAvatar(
                nameBody, emailBody,
                passwordBody, typeBody, phoneBody
            )
        }
        call.enqueue(object:Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>,
                                    response: Response<LoginResponse>) {
                if(response.isSuccessful && response.body()!=null)
                liveData.postValue(response.body())
                else Log.e(TAG,"register@onResponse #${response.code()}")
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG,"register@onFailure",t)
            }

        })
        return liveData
    }

}