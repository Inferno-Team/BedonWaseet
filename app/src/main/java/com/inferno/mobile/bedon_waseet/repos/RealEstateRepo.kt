package com.inferno.mobile.bedon_waseet.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.responses.MessageResponse
import com.inferno.mobile.bedon_waseet.responses.OwnerRealEstateResponse
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.responses.RealEstateRoomImages
import com.inferno.mobile.bedon_waseet.utils.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


object RealEstateRepo {
    private const val TAG = "RealEstateRepo"

    fun getRecentRealEstates(token: String): MutableLiveData<ArrayList<RealEstate>> {
        val _liveData = MutableLiveData<ArrayList<RealEstate>>()
        BaseApplication.service.getRecentRealEstate("Bearer $token")
            .enqueue(object : Callback<ArrayList<RealEstate>> {
                override fun onResponse(
                    call: Call<ArrayList<RealEstate>>,
                    response: Response<ArrayList<RealEstate>>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        _liveData.postValue(response.body())
                    else Log.e(TAG, "getRecentRealEstates@onFailure#${response.code()}")
                }

                override fun onFailure(call: Call<ArrayList<RealEstate>>, t: Throwable) {
                    Log.e(TAG, "getRecentRealEstates@onFailure", t)
                }

            })
        return _liveData
    }

    fun getOwnerMyRealEstate(token: String): MutableLiveData<ArrayList<RealEstate>> {
        val _liveData = MutableLiveData<ArrayList<RealEstate>>()
        BaseApplication.service.getOwnerMyRealEstate("Bearer $token")
            .enqueue(object : Callback<OwnerRealEstateResponse> {
                override fun onResponse(
                    call: Call<OwnerRealEstateResponse>,
                    response: Response<OwnerRealEstateResponse>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        if (body.estates != null)
                            _liveData.postValue(body.estates!!)
                        else Log.e(TAG, "owner@onResponse@body # ${response.message()}")
                    } else Log.e(TAG, "owner@onResponse # ${response.code()}")
                }

                override fun onFailure(call: Call<OwnerRealEstateResponse>, t: Throwable) {
                    Log.e(TAG, "owner@onFailure", t)
                }

            })
        return _liveData
    }

    fun addRealEstate(
        token: String, location: String,
        direction: String, type: String,
        _long: Double, lat: Double, price: Double,
        buyType: String, area: Double,
    ): MutableLiveData<MessageResponse> {
        val addRealEstateLiveData = MutableLiveData<MessageResponse>()
        BaseApplication.service.addRealEstate(
            "Bearer $token", _long,
            lat, type, location, buyType, price,
            direction, area
        )
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        addRealEstateLiveData.postValue(response.body())
                    else Log.e(TAG, "addRealEstate@onResponse #${response.code()}")
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "addRealEstate@onFailure", t);
                }
            })
        return addRealEstateLiveData
    }

    fun getRealEstateDetails(id: Int): MutableLiveData<RealEstate> {
        val _estateLiveData = MutableLiveData<RealEstate>()
        BaseApplication.service.estateDetails(id)

            .enqueue(object : Callback<RealEstate> {
                override fun onResponse(call: Call<RealEstate>, response: Response<RealEstate>) {
                    if (response.isSuccessful && response.body() != null)
                        _estateLiveData.postValue(response.body())
                    else Log.e(TAG, "getRealEstateDetails@onResponse #${response.code()}")
                }

                override fun onFailure(call: Call<RealEstate>, t: Throwable) {
                    Log.e(TAG, "getRealEstateDetails@onFailure", t)
                }
            })
        return _estateLiveData
    }


    fun editRealEstateImage(
        token: String,
        image: File,
        id: Int,
        l: UploadCallbacks
    ): MutableLiveData<MessageResponse> {
        val _editImage = MutableLiveData<MessageResponse>()
        val imageBody = ProgressRequestBody(image, l, "image")
        val filePart = MultipartBody.Part.createFormData("image", image.name, imageBody)
        val idBody: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            id.toString()
        )
        BaseApplication.service.editEstateImage("Bearer $token", filePart, idBody)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        _editImage.postValue(response.body())
                    else Log.e(TAG, "editRealEstateImage@onResponse #${response.code()}")
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "editRealEstateImage@onFailure", t)
                }
            })
        return _editImage
    }

    fun editVideo360(
        token: String,
        video: File,
        id: Int,
        l: UploadCallbacks
    ): MutableLiveData<MessageResponse> {
        val _editImage = MutableLiveData<MessageResponse>()
        val imageBody = ProgressRequestBody(video, l, "video")
        val filePart = MultipartBody.Part.createFormData("video", video.name, imageBody)
        val idBody: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            id.toString()
        )
        BaseApplication.service.editVideo360("Bearer $token", filePart, idBody)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        _editImage.postValue(response.body())
                    else Log.e(TAG, "editRealEstateImage@onResponse #${response.code()}")
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "editRealEstateImage@onFailure", t)
                }
            })
        return _editImage
    }


    fun uploadImagesToServer(
        token: String,
        estateId: Int,
        roomName: String,
        images: ArrayList<RealEstateRoomImages>
    ): MutableLiveData<MessageResponse> {
        val _uploadRoomImages = MutableLiveData<MessageResponse>()
        val roomNameBody = roomName.toRequestBody("text/plan".toMediaTypeOrNull())
        val estateIdBody = "$estateId".toRequestBody("text/plan".toMediaTypeOrNull())
        val imagesParts = arrayListOf<MultipartBody.Part>()
        for (image in images) {
            if (!image.url.contains("http://")) {
                // it's a file we need to upload it
                val file = File(image.url)
                if (file.exists()) {
                    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    imagesParts.add(
                        MultipartBody.Part.createFormData("image[]", file.name, requestBody)
                    )
                }
            }
        }

        BaseApplication.service.uploadRoomImages(
            "Bearer $token",
            imagesParts,
            estateIdBody,
            roomNameBody
        )
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        _uploadRoomImages.postValue(response.body())
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "onUploadIMages:onFailure", t)
                }

            })
        return _uploadRoomImages
    }


    fun searchRealEstate(token: String, word: String): MutableLiveData<ArrayList<RealEstate>> {
        val liveData = MutableLiveData<ArrayList<RealEstate>>()
        BaseApplication.service.search(token, word)
            .enqueue(object : Callback<ArrayList<RealEstate>> {
                override fun onResponse(
                    call: Call<ArrayList<RealEstate>>,
                    response: Response<ArrayList<RealEstate>>
                ) {
                    liveData.postValue(response.body())
                }

                override fun onFailure(call: Call<ArrayList<RealEstate>>, t: Throwable) {
                    Log.e(TAG, "onFailure", t)
                }

            })
        return liveData
    }

    fun getREalEstateType(token: String): MutableLiveData<ArrayList<RealEstateType>> {
        val liveData = MutableLiveData<ArrayList<RealEstateType>>()
        BaseApplication.service.getTypes(token)
            .enqueue(object : Callback<ArrayList<RealEstateType>> {
                override fun onResponse(
                    call: Call<ArrayList<RealEstateType>>,
                    response: Response<ArrayList<RealEstateType>>
                ) {
                    liveData.postValue(response.body())
                }

                override fun onFailure(call: Call<ArrayList<RealEstateType>>, t: Throwable) {
                    Log.e(TAG, "getREalEstateType@onFailure", t)
                }

            })
        return liveData
    }

    fun filterByBuyType(token: String, type: String):
            MutableLiveData<ArrayList<RealEstate>> {
        val liveData = MutableLiveData<ArrayList<RealEstate>>()
        BaseApplication.service.filterByBuyType("Bearer $token", type)
            .enqueue(object : Callback<ArrayList<RealEstate>> {
                override fun onResponse(
                    call: Call<ArrayList<RealEstate>>,
                    response: Response<ArrayList<RealEstate>>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        liveData.postValue(response.body())
                    else Log.e(TAG, "filterByBuyType@onResponse #${response.code()}")

                }

                override fun onFailure(call: Call<ArrayList<RealEstate>>, t: Throwable) {
                    Log.e(TAG, "filterByBuyType@onFailure", t)

                }

            })

        return liveData
    }

    fun filterByEstateType(token: String, type: String):
            MutableLiveData<ArrayList<RealEstate>> {
        val liveData = MutableLiveData<ArrayList<RealEstate>>()
        BaseApplication.service.filterByEstateType("Bearer $token", type)
            .enqueue(object : Callback<ArrayList<RealEstate>> {
                override fun onResponse(
                    call: Call<ArrayList<RealEstate>>,
                    response: Response<ArrayList<RealEstate>>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        liveData.postValue(response.body())
                    else Log.e(TAG, "filterByEstateType@onResponse #${response.code()}")
                }

                override fun onFailure(call: Call<ArrayList<RealEstate>>, t: Throwable) {
                    Log.e(TAG, "filterByEstateType@onFailure", t)
                }

            })
        return liveData
    }


    fun getRealEstateInsideCircle(token: String, lat: Double, lng: Double)
            : MutableLiveData<ArrayList<RealEstate>> {
        val livedata = MutableLiveData<ArrayList<RealEstate>>()
        BaseApplication.service.getInsideCircle(
            "Bearer $token",
            lat, lng
        )
            .enqueue(object : Callback<ArrayList<RealEstate>> {
                override fun onResponse(
                    call: Call<ArrayList<RealEstate>>,
                    response: Response<ArrayList<RealEstate>>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        livedata.postValue(response.body())
                    else Log.e(TAG, "insideCircle@onResponse #code:${response.code()}")
                }

                override fun onFailure(call: Call<ArrayList<RealEstate>>, t: Throwable) {
                    Log.e(TAG, "insideCircle@onFailure", t)
                }

            })
        return livedata
    }

    fun getRealEstateFullFiltered(
        token: String,
        buyType: String,
        estateType: String,
        priceRange: List<Float>,
        direction: String
    ): MutableLiveData<ArrayList<RealEstate>> {
        val liveData = MutableLiveData<ArrayList<RealEstate>>()
        BaseApplication.service.filterFull(
            "Bearer $token",
            buyType,
            estateType,
            priceRange,
            direction
        )
            .enqueue(object : Callback<ArrayList<RealEstate>> {
                override fun onResponse(
                    call: Call<ArrayList<RealEstate>>,
                    response: Response<ArrayList<RealEstate>>
                ) {
                    if (response.isSuccessful && response.body() != null)
                        liveData.postValue(response.body())
                    else Log.e(TAG, "getRealEstateFullFiltered@onResponse #code:${response.code()}")
                }

                override fun onFailure(call: Call<ArrayList<RealEstate>>, t: Throwable) {
                    Log.e(TAG, "getRealEstateFullFiltered@onFailure", t)
                }

            })

        return liveData
    }


}