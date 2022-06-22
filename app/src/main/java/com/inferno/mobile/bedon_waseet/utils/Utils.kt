package com.inferno.mobile.bedon_waseet.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException


private const val SHARED_NAME = "shared"
private const val LOGGED_IN = "logged_in"
private const val USER_TYPE = "user_type"
private const val TOKEN = "token"

fun isUserLoggedIn(context: Context): Boolean {
    return shared(context).getBoolean(LOGGED_IN, false)
}

fun logIn(context: Context, token: String, type: UserType) {
    sharedEdit(shared(context))
        .putString(TOKEN, token)
        .putString(USER_TYPE, type.name)
        .putBoolean(LOGGED_IN, true)
        .apply()
}

fun getToken(context: Context): String {
    return shared(context).getString(TOKEN, "") ?: ""
}

fun getUserType(context: Context): UserType? {
    return if (isUserLoggedIn(context))
        UserType.valueOf(shared(context).getString(USER_TYPE, UserType.Gust.name)!!)
    else null
}

fun logOut(context: Context) {
    sharedEdit(shared(context))
        .remove(TOKEN)
        .remove(USER_TYPE)
        .remove(LOGGED_IN)
        .apply()
}

private fun shared(context: Context): SharedPreferences {
    return context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
}

private fun sharedEdit(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
    return sharedPreferences.edit()
}


interface UploadCallbacks {
    fun onProgressUpdate(percentage: Int)
    fun onError()
    fun onFinish()
}

class ProgressRequestBody(
    private val mFile: File,
    private val mListener: UploadCallbacks,
    private val contentType: String,
) : RequestBody() {

    private val DEFAULT_BUFFER_SIZE = 2048

    override fun contentType(): MediaType? {
        return "$contentType/*".toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val _in = FileInputStream(mFile)
        var uploaded: Long = 0
        try {
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (_in.read(buffer).also { read = it } != -1) {

                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength, mListener))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        } finally {
            _in.close()
        }
    }

    private class ProgressUpdater(
        private val mUploaded: Long, private val mTotal: Long,
        val mListener: UploadCallbacks
    ) :
        Runnable {
        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }
}
