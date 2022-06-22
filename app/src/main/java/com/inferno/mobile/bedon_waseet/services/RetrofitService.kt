package com.inferno.mobile.bedon_waseet.services

import androidx.annotation.Nullable
import com.inferno.mobile.bedon_waseet.responses.LoginResponse
import com.inferno.mobile.bedon_waseet.responses.MessageResponse
import com.inferno.mobile.bedon_waseet.responses.OwnerRealEstateResponse
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.RealEstateType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {

    @POST("api/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("api/signup")
    @Multipart
    fun registerWithAvatar(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("type") type: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part images: MultipartBody.Part,
    ): Call<LoginResponse>

    @POST("api/signup")
    @Multipart
    fun registerWithoutAvatar(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("type") type: RequestBody,
        @Part("phone") phone: RequestBody,
    ): Call<LoginResponse>


    @GET("api/recent_real_estate")
    fun getRecentRealEstate(@Header("Authorization") token: String):
            Call<ArrayList<RealEstate>>

    @GET("api/get_my_real_estate")
    fun getOwnerMyRealEstate(@Header("Authorization") token: String)
            : Call<OwnerRealEstateResponse>

    @POST("api/add_real_estate")
    @FormUrlEncoded
    fun addRealEstate(
        @Header("Authorization") token: String,
        @Field("long") _long: Double,
        @Field("lat") lat: Double,
        @Field("type") type: String,
        @Field("location") location: String,
        @Field("buy_type") buyType: String,
        @Field("budget") budget: Double,
        @Field("direction") direction: String,
        @Field("area") area: Double,
    ): Call<MessageResponse>

    @GET("api/get_estate_details/{id}")
    fun estateDetails(@Path("id") id: Int): Call<RealEstate>


    @POST("api/edit_image")
    @Multipart
    fun editEstateImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("id") estateId: RequestBody
    ): Call<MessageResponse>

    @POST("api/edit_video360")
    @Multipart
    fun editVideo360(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("id") estateId: RequestBody
    ): Call<MessageResponse>

    @POST("api/upload_room_images")
    @Multipart
    fun uploadRoomImages(
        @Header("Authorization") token: String,
        @Part images: List<MultipartBody.Part>,
        @Part("estate_id") estateId: RequestBody,
        @Part("room_name") roomName: RequestBody,
    ): Call<MessageResponse>

    @POST("api/search")
    @FormUrlEncoded
    fun search(
        @Header("Authorization") token: String,
        @Field("word") word: String
    ): Call<ArrayList<RealEstate>>

    @POST("api/filter_by_buy_type")
    @FormUrlEncoded
    fun filterByBuyType(
        @Header("Authorization") token: String,
        @Field("type") type: String
    ): Call<ArrayList<RealEstate>>

    @POST("api/filter_by_estate_type")
    @FormUrlEncoded
    fun filterByEstateType(
        @Header("Authorization") token: String,
        @Field("type") type: String
    ): Call<ArrayList<RealEstate>>

    @POST("api/filter_full")
    @FormUrlEncoded
    fun filterFull(
        @Header("Authorization") token: String,
        @Field("buyType") buyType: String,
        @Field("estateType") estateType: String,
        @Field("priceRange[]") priceRange: List<Float>,
        @Field("direction") direction: String,
        ): Call<ArrayList<RealEstate>>


    @GET("api/get_types")
    fun getTypes(@Header("Authorization") token: String): Call<ArrayList<RealEstateType>>


    @GET("api/get_real_estate_inside_circle")
    fun getInsideCircle(
        @Header("Authorization") token: String,
        @Query("p_lat") lat: Double,
        @Query("p_long") lng: Double
    ): Call<ArrayList<RealEstate>>
}