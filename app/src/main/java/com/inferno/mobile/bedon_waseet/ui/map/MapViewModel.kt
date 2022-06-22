package com.inferno.mobile.bedon_waseet.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inferno.mobile.bedon_waseet.repos.RealEstateRepo
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.mapbox.geojson.Point

class MapViewModel : ViewModel() {
    private val repo = RealEstateRepo

    fun getRealEstateInsideCircle(token:String,lat:Double,lng:Double):
            LiveData<ArrayList<RealEstate>> {
       return repo.getRealEstateInsideCircle(token, lat, lng)
    }


}