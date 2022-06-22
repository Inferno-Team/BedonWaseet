package com.inferno.mobile.bedon_waseet.ui.owner.my_realestates

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inferno.mobile.bedon_waseet.repos.RealEstateRepo
import com.inferno.mobile.bedon_waseet.responses.MessageResponse
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.BuyType

class MyEstateViewModel : ViewModel() {
    private val repo = RealEstateRepo

    fun getMyRealEstates(token: String): LiveData<ArrayList<RealEstate>> {
       return  repo.getOwnerMyRealEstate(token)
    }

    fun addRealEstate(
        token: String, location: String,
        direction: String, type: String, _long: Double,
        lat: Double, price: Double,
        buyType: String, area: Double
    ): LiveData<MessageResponse> {
        return repo.addRealEstate(
            token, location, direction,
            type, _long, lat, price, buyType, area
        )
    }
}