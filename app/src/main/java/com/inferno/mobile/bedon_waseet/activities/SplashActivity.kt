package com.inferno.mobile.bedon_waseet.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.SplashActivityBinding
import com.inferno.mobile.bedon_waseet.databinding.SplashActivityLayoutBinding
import com.inferno.mobile.bedon_waseet.utils.isUserLoggedIn


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: SplashActivityLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = SplashActivityLayoutBinding.inflate(layoutInflater)
        val _intent = Intent(this, MainActivity::class.java)
        if (isUserLoggedIn(this)) {
            startActivity(_intent)
            finish()
        }
        setContentView(binding.root)

    }
}
