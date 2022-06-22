package com.inferno.mobile.bedon_waseet.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inferno.mobile.bedon_waseet.activities.SplashActivity
import com.inferno.mobile.bedon_waseet.databinding.SettingsLayoutBinding
import com.inferno.mobile.bedon_waseet.utils.logOut

class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsLayoutBinding.inflate(inflater, container, false)
        binding.signOutBtn.setOnClickListener {
            logOut(requireContext())
            val intent = Intent(requireActivity(), SplashActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }
}