package com.inferno.mobile.bedon_waseet.ui.owner.edit_estate

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.spherical.SphericalGLSurfaceView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.activities.MainActivity
import com.inferno.mobile.bedon_waseet.adapters.EditRoomAdapter
import com.inferno.mobile.bedon_waseet.adapters.OnAddRoomClickListener
import com.inferno.mobile.bedon_waseet.adapters.OnSendToServer
import com.inferno.mobile.bedon_waseet.databinding.AddRoomLayoutBinding
import com.inferno.mobile.bedon_waseet.databinding.EstateEditLayoutBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.responses.RealEstateRoomImages
import com.inferno.mobile.bedon_waseet.responses.RealEstateRooms
import com.inferno.mobile.bedon_waseet.utils.RealPathUtil
import com.inferno.mobile.bedon_waseet.utils.UploadCallbacks
import com.inferno.mobile.bedon_waseet.utils.getToken
import java.io.File
import java.io.FileOutputStream


class EditEstateFragment : Fragment() {


    private lateinit var binding: EstateEditLayoutBinding
    private val viewModel: EditEstateViewModel by viewModels()
    private val ESTATE_IMAGE360_REQUEST_CODE = 100
    private val ESTATE_IMAGE_REQUEST_CODE = 101
    private val ESTATE_ROOM_IMAGES_REQUEST_CODE = 102
    private var thread: TimerThread? = null
    private var upload360thread: TimerThread? = null
    private lateinit var estate: RealEstate
    private var roomId = -1
    private lateinit var controller: NavController
    private var video360path: String? = null
    private lateinit var player: ExoPlayer


    override fun onDetach() {
        super.onDetach()
        (requireActivity() as MainActivity).viewBinding.bottomNavView.visibility = View.VISIBLE
        if (thread != null && thread!!.isAlive)
            thread!!.interrupt()
        if (upload360thread != null && upload360thread!!.isAlive)
            upload360thread!!.interrupt()

    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).viewBinding.bottomNavView.visibility = View.GONE
//        binding.video360.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EstateEditLayoutBinding.inflate(inflater, container, false)

        estate = requireArguments().getSerializable("estate") as RealEstate
        controller = findNavController()
        loadEstateIntoView()
        binding.editRealEstate.setOnClickListener {

            if (viewModel.uploadModel.value != null && viewModel.uploadModel.value!!) {
                // upload to server
                // we need to stop the thread
                // start new thread and apply the upload method
                Toast.makeText(context, "Uploading", Toast.LENGTH_LONG).show()
                thread!!.interrupt()
                binding.circleProgress.progress = 0F
                uploadEstateImage()
            } else {
                // open image chooser
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Choose Estate Image"),
                    ESTATE_IMAGE_REQUEST_CODE
                )
            }

        }
        binding.edit360.setOnClickListener {
            if (viewModel.upload360.value != null && viewModel.upload360.value!!) {
                // upload to server
                // we need to stop the thread
                // start new thread and apply the upload method
                Toast.makeText(context, "Uploading", Toast.LENGTH_LONG).show()
                upload360thread!!.interrupt()
                binding.circleProgress.progress = 0F
                upload360()
            } else {
                // open image chooser
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                intent.putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    arrayOf("video/mp4")
                )
                startActivityForResult(
                    Intent.createChooser(intent, "Choose 360 degree Video"),
                    ESTATE_IMAGE360_REQUEST_CODE
                )
            }
        }
        viewModel.uploadModel.observe(requireActivity(), this::onModeChange)
        viewModel.upload360.observe(requireActivity(), this::onModeChange360)
        binding.addRoom.setOnClickListener {
            addRoomLayout()
        }
        viewModel.selectImageLiveData.observe(requireActivity()) {
            if (roomId != -1) {
                val list = ArrayList<RealEstateRoomImages>()
                for (file in it!!) {
                    list.add(RealEstateRoomImages(file.absolutePath))
                }
                (binding.rooms.adapter as EditRoomAdapter).addImages(list, roomId)
            }

        }
        return binding.root
    }


    private fun addRoomLayout() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomBinding = AddRoomLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomBinding.root)
        bottomSheetDialog.show()
        bottomBinding.addBtn.setOnClickListener {
            val name = bottomBinding.roomName.text.toString()
            val room = RealEstateRooms(-1, name, arrayListOf())
            val adapter = (binding.rooms.adapter as EditRoomAdapter)
            adapter.addRoom(room)
            binding.rooms.visibility = View.VISIBLE
            binding.noRoom.visibility = View.GONE
            bottomSheetDialog.dismiss()
        }
    }

    private fun onModeChange(value: Boolean) {
        if (value) {
            binding.editRealEstate.setImageResource(R.drawable.ic_baseline_check_24)
            binding.circleProgress.visibility = View.VISIBLE
            if (thread != null)
                thread!!.interrupt()
            thread = TimerThread(requireActivity(), binding, viewModel)
            thread!!.start()
        } else {
            binding.editRealEstate.setImageResource(R.drawable.ic_baseline_edit_24)
            binding.circleProgress.visibility = View.GONE
            Glide.with(requireContext())
                .load("${BaseApplication.BASE_URL}${estate.image}")
                .centerCrop()
                .placeholder(
                    AppCompatResources
                        .getDrawable(requireContext(), R.drawable.real_estate_logo)
                )
                .error(
                    AppCompatResources
                        .getDrawable(requireContext(), R.drawable.real_estate_logo)
                )
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imgRealEstate)
        }
    }

    private fun onModeChange360(value: Boolean) {
        val uri: Uri
        val mediaSource: MediaSource
        if (value) {
            uri = Uri.parse(video360path)
            mediaSource = buildMediaSource(uri)
            binding.edit360.setImageResource(R.drawable.ic_baseline_check_24)
            binding.circleProgress360.visibility = View.VISIBLE
            if (upload360thread != null)
                upload360thread!!.interrupt()
            upload360thread = TimerThread(requireActivity(), binding, viewModel, true)
            upload360thread!!.start()
        } else {
            binding.edit360.setImageResource(R.drawable.ic_baseline_edit_24)
            binding.circleProgress360.visibility = View.GONE
            uri = Uri.parse(BaseApplication.BASE_URL + estate.image360)
            mediaSource = buildMediaSource(uri)
        }
        player.release()
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.prepare(mediaSource)
        binding.video360.player = player
    }


    private fun loadEstateIntoView() {
        (binding.video360.videoSurfaceView as SphericalGLSurfaceView)
            .setDefaultStereoMode(C.STEREO_MODE_LEFT_RIGHT)
        val uri = Uri.parse(BaseApplication.BASE_URL + estate.image360)
        val mediaSource = buildMediaSource(uri)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.prepare(mediaSource)
        binding.video360.player = player


        Glide.with(requireContext())
            .load("${BaseApplication.BASE_URL}${estate.image}")
            .centerCrop()
            .placeholder(
                AppCompatResources
                    .getDrawable(requireContext(), R.drawable.real_estate_logo)
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imgRealEstate)
        var adapter = EditRoomAdapter(arrayListOf())
        if (estate.rooms == null || estate.rooms!!.isEmpty()) {
            binding.rooms.visibility = View.GONE
            binding.noRoom.visibility = View.VISIBLE
        } else {
            binding.noRoom.visibility = View.GONE
            binding.rooms.visibility = View.VISIBLE
            adapter = EditRoomAdapter(estate.rooms!!)
        }
        binding.rooms.adapter = adapter
        adapter.setOnAddRoomClickListener(object : OnAddRoomClickListener {
            override fun onClick(id: Int) {
                roomId = id
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(
                    Intent.createChooser(intent, "Select Room Images"),
                    ESTATE_ROOM_IMAGES_REQUEST_CODE
                )
            }
        })
        adapter.setOnSendClickListener(object : OnSendToServer {
            override fun onLongClick(roomName: String, images: ArrayList<RealEstateRoomImages>) {
                viewModel.setImagesToServer(
                    getToken(requireContext()),
                    estate.id,
                    roomName,
                    images
                ).observe(requireActivity()) {
                    Toast.makeText(requireContext(), it!!.msg, Toast.LENGTH_LONG).show()
                }
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ESTATE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    val imageUri = data.data!!
                    copyImageAndShow(imageUri)
                }
            }
        } else {
            if (requestCode == ESTATE_ROOM_IMAGES_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    val list = arrayListOf<File>()
                    if (data != null) {
                        if (data.clipData != null) {
                            val count: Int = data.clipData!!.itemCount
                            for (i in 0 until count) {
                                val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                                val now = System.currentTimeMillis()
                                val imageFile = copyImageAndShow(imageUri, now.toString())
                                list.add(imageFile)
                            }
                        } else {
                            if (data.data != null) {
                                val imageUri = data.data!!
                                val now = System.currentTimeMillis()
                                val imageFile = copyImageAndShow(imageUri, now.toString())
                                list.add(imageFile)
                            }
                        }
                        viewModel.selectImageLiveData.postValue(list)
                    }
                }
            }
        }

        if (requestCode == ESTATE_IMAGE360_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    val img360 = data.data!!
                    val path = RealPathUtil.getRealPath(requireContext(), img360)
                    showVideo360(path!!)
                }
            }
        }
    }

    private fun showVideo360(path: String) {
        video360path = path
        viewModel.upload360.postValue(true)

    }


    private fun copyImageAndShow(imageUri: Uri) {

        val file = copyImageAndShow(imageUri, "estate")
        viewModel.uploadModel.postValue(true)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        Glide.with(requireContext())
            .load(bitmap)
            .centerCrop()
            .placeholder(
                AppCompatResources
                    .getDrawable(requireContext(), R.drawable.real_estate_logo)
            )
            .error(
                AppCompatResources
                    .getDrawable(requireContext(), R.drawable.real_estate_logo)
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imgRealEstate)

    }

    private fun copyImageAndShow(imageUri: Uri, now: String = "estate"): File {
        val cache = requireActivity().cacheDir!!
        val imageFile = File(cache.path + "/$now.jpg")
        imageFile.createNewFile()
        val outputStream = FileOutputStream(imageFile)
        val imageStream = requireContext().contentResolver.openInputStream(imageUri)!!
        val bitmap = BitmapFactory.decodeStream(imageStream)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        bitmap.recycle()
        return imageFile
    }

    private fun uploadEstateImage() {
        // check image file if exists
        val cache = requireContext().cacheDir
        val image = File(cache.path + "/estate.jpg")
        if (image.exists()) {
            binding.circleProgress.visibility = View.VISIBLE
            viewModel.editImage(getToken(requireContext()), image,
                estate.id,
                object : UploadCallbacks {
                    override fun onProgressUpdate(percentage: Int) {
                        println("upload $percentage")
                        binding.circleProgress.progress = percentage.toFloat()
                    }

                    override fun onError() {
                        println("Error On Upload")
                    }

                    override fun onFinish() {
                        println("Finish Uploading")
                    }

                })
                .observe(requireActivity()) {
                    if (it != null) {
                        Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                        binding.editRealEstate.setImageResource(R.drawable.ic_baseline_edit_24)
                        binding.circleProgress.visibility = View.GONE
                    }
                }
        } else {
            Toast.makeText(context, "Image File not found", Toast.LENGTH_LONG).show()
        }
    }

    private fun upload360() {
        // check image file if exists
        if (video360path == null) return
        val video = File(video360path!!)
        if (video.exists()) {
            binding.circleProgress360.visibility = View.VISIBLE
            viewModel.edit360(getToken(requireContext()), video,
                estate.id,
                object : UploadCallbacks {
                    override fun onProgressUpdate(percentage: Int) {
                        println("upload $percentage")
                        binding.circleProgress360.progress = percentage.toFloat()
                    }

                    override fun onError() {
                        println("Error On Upload")
                    }

                    override fun onFinish() {
                        println("Finish Uploading")
                    }

                })
                .observe(requireActivity()) {
                    if (it != null) {
                        Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                        binding.edit360.setImageResource(R.drawable.ic_baseline_edit_24)
                        binding.circleProgress360.visibility = View.GONE
                    }
                }
        } else {
            Toast.makeText(context, "video 360 File not found", Toast.LENGTH_LONG).show()
        }
    }


    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()

    }

    override fun onPause() {
        super.onPause()
        player.stop()
        player.release()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory =
            DefaultDataSourceFactory(requireActivity(), "javiermarsicano-VR-app")
        // Create a media source using the supplied URI
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }
}

class TimerThread(
    private val context: Activity,
    private val binding: EstateEditLayoutBinding,
    private val viewModel: EditEstateViewModel,
    private var is360: Boolean = false
) : Thread() {

    override fun run() {
        super.run()
        var timer = 10000 // 10 second
        try {
            while (timer > 0 && !this.isInterrupted) {
                sleep(200)
                timer -= 200
                context.runOnUiThread {
                    var time = ((10000 - timer) / 1000) // convert from mi -> second
                    //convert time to percent
                    // 10    -> 100
                    // time  -> x
                    // x = time * 100 / 10 ==> x = (time * 10)%
                    time *= 10
                    println(time)
                    if (is360) {
                        binding.circleProgress360.progress = time.toFloat()
                    } else binding.circleProgress.progress = time.toFloat()
                }
            }
            if (is360) viewModel.upload360.postValue(false)
            else viewModel.uploadModel.postValue(false)
        } catch (e: Exception) {
            e.fillInStackTrace()
        }
    }
}