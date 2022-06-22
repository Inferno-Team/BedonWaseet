package com.inferno.mobile.bedon_waseet.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.responses.LoginResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginRepo {
    private const val TAG = "LoginRepo"

    fun login(email: String, password: String): MutableLiveData<LoginResponse> {
        val liveData = MutableLiveData<LoginResponse>()
        BaseApplication.service.login(email,password)
            .enqueue(object: Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful && response.body()!=null){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e(TAG,"login#onFailure ",t)
                }
            })
        return liveData
    }
}