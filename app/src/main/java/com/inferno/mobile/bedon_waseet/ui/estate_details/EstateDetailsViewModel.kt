package com.inferno.mobile.bedon_waseet.ui.estate_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inferno.mobile.bedon_waseet.repos.RealEstateRepo
import com.inferno.mobile.bedon_waseet.responses.RealEstate

class EstateDetailsViewModel : ViewModel() {
    private var repo = RealEstateRepo
    fun getEstateDetails(id: Int):LiveData<RealEstate> {
        return repo.getRealEstateDetails(id)
    }
}