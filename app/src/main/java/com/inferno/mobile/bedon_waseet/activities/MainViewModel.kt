package com.inferno.mobile.bedon_waseet.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inferno.mobile.bedon_waseet.repos.RealEstateRepo
import com.inferno.mobile.bedon_waseet.responses.RealEstate

class MainViewModel:ViewModel() {
    private val repo = RealEstateRepo
    fun getStateById(id:Int):LiveData<RealEstate>{
        return repo.getRealEstateDetails(id)
    }
}