package com.inferno.mobile.bedon_waseet.ui.estate_details

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBindings
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.spherical.SphericalGLSurfaceView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.activities.MainActivity
import com.inferno.mobile.bedon_waseet.adapters.RealEstateRoomAdapter
import com.inferno.mobile.bedon_waseet.databinding.EstateDetailsFragmentBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import kotlin.math.abs

class EstateDetailsFragment : Fragment() {
    private lateinit var binding: EstateDetailsFragmentBinding
    private lateinit var player: ExoPlayer
    private val viewModel: EstateDetailsViewModel by viewModels()
    private lateinit var url: String

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as MainActivity).viewBinding.bottomNavView.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).viewBinding.bottomNavView.visibility = View.GONE
        binding.video360.onResume()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

        binding = EstateDetailsFragmentBinding.inflate(inflater, container, false)
        binding.fullScreen.visibility = View.INVISIBLE
        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                run {
                    if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                        binding.userContainer.visibility = View.GONE
                        binding.fullScreen.visibility = View.GONE
                    } else {
                        binding.userContainer.visibility = View.VISIBLE
                        binding.fullScreen.visibility = View.VISIBLE
                    }
                }
            })

        binding.fullScreen.setOnClickListener {
//            val bundle = Bundle()

//            bundle.putString("url", url)
//            findNavController()
//                .navigate(R.id.action_estateDetailsFragment_to_image360Fragment, bundle)
        }

        val estate = requireArguments().getSerializable("estate") as RealEstate
        (binding.video360.videoSurfaceView as SphericalGLSurfaceView)
            .setDefaultStereoMode(C.STEREO_MODE_LEFT_RIGHT)

        viewModel.getEstateDetails(estate.id).observe(requireActivity(), this::realEstateDetails)
        return binding.root
    }

    private fun realEstateDetails(realEstate: RealEstate) {
        binding.estate = realEstate
        binding.fullScreen.visibility = View.VISIBLE


        val uri = Uri.parse(BaseApplication.BASE_URL + realEstate.owner.avatar)
        url = BaseApplication.BASE_URL + realEstate.image360
        initializePlayer()
        Glide.with(requireContext())
            .load(uri)
            .centerCrop()
            .placeholder(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.real_estate_logo
                )
            )
            .error(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_marker
                )
            )
            .transform(CircleCrop())
            .into(binding.userLogo)
        val adapter = RealEstateRoomAdapter(realEstate.rooms!!,false)
        binding.rooms.adapter = adapter
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory =
            DefaultDataSourceFactory(requireActivity(), "javiermarsicano-VR-app")
        // Create a media source using the supplied URI
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()

        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)
        player.prepare(mediaSource)

        binding.video360.player = player
//        binding.video360.onResume()
    }

    private fun releasePlayer() {
        binding.video360.onPause()
        player.release()

    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}
