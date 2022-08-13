package com.inferno.mobile.bedon_waseet.responses

import com.google.gson.annotations.SerializedName
import com.inferno.mobile.bedon_waseet.utils.BuyType
import com.inferno.mobile.bedon_waseet.utils.RealEstateType
import com.inferno.mobile.bedon_waseet.utils.UserType
import java.io.Serializable


data class LoginResponse(
    val code: Int,
    val message: String,
    val token: String?,
    val type: UserType
)

data class User(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("type")
    val type: UserType,
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("avatar")
    val avatar: String?
):Serializable


data class RealEstate(
    @SerializedName("id")
    val id:Int,
    @SerializedName("lng")
    val lng: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("type")
    val stateType: RealEstateType,
    @SerializedName("location")
    val location: String,
    @SerializedName("rate")
    val rate: Float,
    @SerializedName("buy_type")
    val buyType: BuyType,
    @SerializedName("budget")
    val budget: Float,
    @SerializedName("img_url")
    val image: String?,
    @SerializedName("img360_url")
    val image360: String?,
    @SerializedName("owner")
    val owner: User,
    @SerializedName("rooms")
    val rooms: ArrayList<RealEstateRooms>?
):Serializable

data class RealEstateRooms(
    @SerializedName("id")
    val id:Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("images")
    val images: ArrayList<RealEstateRoomImages>?
):Serializable

data class RealEstateRoomImages(
    @SerializedName("img_url")
    val url: String,
    @SerializedName("id")
    val id:Int = -1
):Serializable

data class OwnerRealEstateResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val msg: String?,
    @SerializedName("estates")
    val estates: ArrayList<RealEstate>?
)

data class MessageResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val msg: String
)