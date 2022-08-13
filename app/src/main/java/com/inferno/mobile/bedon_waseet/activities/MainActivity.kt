package com.inferno.mobile.bedon_waseet.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.ActivityMainBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.UserType
import com.inferno.mobile.bedon_waseet.utils.getUserType

class MainActivity : AppCompatActivity() {


    lateinit var viewBinding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

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
        val appLinkIntent = intent
        val appLinkData = appLinkIntent.data
        if (appLinkData != null) {
            val id = appLinkData.getQueryParameter("id")
            viewModel.getStateById(id!!.toInt())
                .observe(this
                ) { t ->
                    val bundle = Bundle()
                    bundle.putSerializable("estate", t)
                    navController.navigate(
                        R.id.action_homeFragment2_to_estateDetailsFragment,
                        bundle
                    )
                }


        }
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