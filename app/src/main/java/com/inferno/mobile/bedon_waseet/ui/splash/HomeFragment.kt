package com.inferno.mobile.bedon_waseet.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.SplashActivityBinding

class HomeFragment : Fragment() {
    private lateinit var binding: SplashActivityBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashActivityBinding.inflate(layoutInflater)


        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_signUpFragment)
        }
        return binding.root
    }
}