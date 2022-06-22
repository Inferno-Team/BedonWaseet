package com.inferno.mobile.bedon_waseet.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.ActivityMainBinding
import com.inferno.mobile.bedon_waseet.utils.UserType
import com.inferno.mobile.bedon_waseet.utils.getUserType
import com.inferno.mobile.bedon_waseet.utils.isUserLoggedIn

class MainActivity : AppCompatActivity() {


    public lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        inflateBottomMenu()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(viewBinding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(viewBinding.bottomNavView, navController)
        viewBinding.bottomNavView.setupWithNavController(navController)
    }
    
    private fun inflateBottomMenu() {
        val userType = getUserType(this)
        viewBinding.bottomNavView.menu.clear()
        if (userType != null) {
            if (userType == UserType.Owner) {
                viewBinding.bottomNavView.inflateMenu(R.menu.owner_nav_menu)
            } else
                viewBinding.bottomNavView.inflateMenu(R.menu.nav_menu)

        } else viewBinding.bottomNavView.inflateMenu(R.menu.nav_menu)
    }
}