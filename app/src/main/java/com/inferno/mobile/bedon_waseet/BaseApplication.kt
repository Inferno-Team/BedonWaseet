package com.inferno.mobile.bedon_waseet

import android.app.Application
import com.inferno.mobile.bedon_waseet.services.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class BaseApplication : Application() {

    companion object {
//        const val BASE_URL = "http://192.168.43.113:8000"
//        const val BASE_URL = "http://192.168.1.106:8000"
//        const val BASE_URL = "http://online-realestate.000webhostapp.com"
        const val BASE_URL = "http://online-realestate2022.000webhostapp.com"
        // val BASE_URL = "http://192.168.1.4"

        lateinit var service: RetrofitService
    }

    init {

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("$BASE_URL/")
            .build()
        service = retrofit.create(RetrofitService::class.java)
    }
}