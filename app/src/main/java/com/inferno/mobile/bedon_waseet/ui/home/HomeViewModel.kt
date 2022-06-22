package com.inferno.mobile.bedon_waseet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inferno.mobile.bedon_waseet.repos.RealEstateRepo
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.RealEstateType
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repo = RealEstateRepo
    fun getRecentRealEstate(token: String): LiveData<ArrayList<RealEstate>> {
        return repo.getRecentRealEstates(token)
    }
    fun search(token:String,word:String):LiveData<ArrayList<RealEstate>>{
        return repo.searchRealEstate(token,word)
    }
    fun getRealEstateTypes(token:String):LiveData<ArrayList<RealEstateType>>{
        return repo.getREalEstateType(token)
    }
}