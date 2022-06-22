package com.inferno.mobile.bedon_waseet.ui.filtered_real_estate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inferno.mobile.bedon_waseet.repos.RealEstateRepo
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.BuyType
import com.inferno.mobile.bedon_waseet.utils.DirectionType
import com.inferno.mobile.bedon_waseet.utils.RealEstateType

class FilteredViewModel() : ViewModel() {
    private val estateRepo = RealEstateRepo

    fun getEstateByBuyType(token: String, type: String): LiveData<ArrayList<RealEstate>> {
        return estateRepo.filterByBuyType(token, type)
    }

    fun getEstateByEstateType(token: String, type: String): LiveData<ArrayList<RealEstate>> {
        return estateRepo.filterByEstateType(token, type)
    }

    fun getEstateFullFilter(
        token: String,
        buyType: String,
        estateType: String,
        priceRange: List<Float>,
        direction: String
    ): LiveData<ArrayList<RealEstate>> {
        return estateRepo.getRealEstateFullFiltered(token,buyType,estateType,priceRange,direction)
    }
}