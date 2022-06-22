package com.inferno.mobile.bedon_waseet.ui.owner.edit_estate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inferno.mobile.bedon_waseet.repos.RealEstateRepo
import com.inferno.mobile.bedon_waseet.responses.MessageResponse
import com.inferno.mobile.bedon_waseet.responses.RealEstateRoomImages
import com.inferno.mobile.bedon_waseet.utils.UploadCallbacks
import java.io.File

class EditEstateViewModel : ViewModel() {
    private val repo = RealEstateRepo

    val uploadModel = MutableLiveData(false)
    val upload360 = MutableLiveData(false)
    val selectImageLiveData = MutableLiveData<ArrayList<File>>()


    fun editImage(token: String, image: File, id: Int, progress: UploadCallbacks)
            : LiveData<MessageResponse> {
        return repo.editRealEstateImage(token, image, id, progress)
    }

    fun edit360(token: String, video: File, id: Int, progress: UploadCallbacks)
            : LiveData<MessageResponse> {
        return repo.editVideo360(token,video,id,progress)
    }

    fun setImagesToServer(
        token: String, estateId: Int, roomName: String,
        images: ArrayList<RealEstateRoomImages>
    ): LiveData<MessageResponse> {
        return repo.uploadImagesToServer(token, estateId, roomName, images)
    }
}