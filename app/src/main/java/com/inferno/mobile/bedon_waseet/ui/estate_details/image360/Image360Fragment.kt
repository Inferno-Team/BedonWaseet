package com.inferno.mobile.bedon_waseet.ui.estate_details.image360

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inferno.mobile.bedon_waseet.activities.MainActivity
import com.inferno.mobile.bedon_waseet.databinding.Image360fragmentBinding

class Image360Fragment : Fragment() {
    private lateinit var binding: Image360fragmentBinding
    private lateinit var player: ExoPlayer
    override fun onDetach() {
        super.onDetach()
        (requireActivity() as MainActivity).viewBinding.bottomNavView.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).viewBinding.bottomNavView.visibility = View.GONE
        binding.video360.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                    findNavController().navigateUp()
                    findNavController().navigateUp()
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        binding = Image360fragmentBinding.inflate(inflater, container, false)


        val url = requireArguments().getString("url")!!
        initializePlayer(url)
        return binding.root
    }


    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory =
            DefaultDataSourceFactory(requireActivity(), "javiermarsicano-VR-app")
        // Create a media source using the supplied URI
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun initializePlayer(url: String) {
        player = SimpleExoPlayer.Builder(requireContext()).build()

        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)
        player.prepare(mediaSource)

        binding.video360.player = player
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