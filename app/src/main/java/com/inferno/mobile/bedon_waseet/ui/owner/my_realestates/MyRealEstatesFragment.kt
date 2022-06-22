package com.inferno.mobile.bedon_waseet.ui.owner.my_realestates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.adapters.RealEstateAdapter
import com.inferno.mobile.bedon_waseet.adapters.RealEstateItemClick
import com.inferno.mobile.bedon_waseet.databinding.MyRealEstateFragmentBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.getToken

class MyRealEstatesFragment : Fragment() {
    private lateinit var binding: MyRealEstateFragmentBinding
    private val viewModel: MyEstateViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyRealEstateFragmentBinding.inflate(inflater, container, false)
        val token = getToken(requireContext())
        viewModel.getMyRealEstates(token).observe(requireActivity(), this::onMyEstate)
        val navController = findNavController()
        binding.addRealEstateBtn.setOnClickListener {
            navController.navigate(R.id.action_nav_my_states_to_addRealEstateFragment)
        }
        return binding.root
    }

    private fun onMyEstate(list: ArrayList<RealEstate>) {
        if (context != null) {
            val adapter = RealEstateAdapter(list)
            adapter.setItemClickListener(object : RealEstateItemClick<RealEstate> {
                override fun onClick(estate: RealEstate) {
                    val bundle = Bundle()
                    bundle.putSerializable("estate", estate)
                    findNavController().navigate(
                        R.id.action_nav_my_states_to_editEstateFragment,
                        bundle
                    )
                }

            })
            binding.rvRealEstate.adapter = adapter
        }
        println(context)
    }
}
