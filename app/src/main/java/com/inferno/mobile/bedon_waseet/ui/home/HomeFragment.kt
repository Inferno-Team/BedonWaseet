package com.inferno.mobile.bedon_waseet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inferno.mobile.bedon_waseet.adapters.RealEstateAdapter
import com.inferno.mobile.bedon_waseet.adapters.RealEstateItemClick
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.databinding.HomeFragmentBinding
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.utils.getToken

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val bottom = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        if (bottom != null)
            bottom.visibility = View.VISIBLE

        viewModel.getRecentRealEstate(getToken(requireContext()))
            .observe(requireActivity(), this::realEstateResponse)
        return binding.root
    }

    private fun realEstateResponse(list: ArrayList<RealEstate>) {
        if (context != null) {
            val adapter = RealEstateAdapter(list)
            adapter.setItemClickListener(object : RealEstateItemClick<RealEstate> {
                override fun onClick(estate: RealEstate) {
                    val b = Bundle()
                    b.putSerializable("estate", estate)
                    findNavController().navigate(R.id.action_nav_home_to_estateDetailsFragment, b)
                }

            })
            binding.rvRealEstate.adapter = adapter
        }
    }

}