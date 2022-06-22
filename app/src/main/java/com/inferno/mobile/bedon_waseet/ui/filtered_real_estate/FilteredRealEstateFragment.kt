package com.inferno.mobile.bedon_waseet.ui.filtered_real_estate

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
import com.inferno.mobile.bedon_waseet.databinding.HomeFragmentBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.BuyType
import com.inferno.mobile.bedon_waseet.utils.DirectionType
import com.inferno.mobile.bedon_waseet.utils.RealEstateType
import com.inferno.mobile.bedon_waseet.utils.getToken

class FilteredRealEstateFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding
    private val viewModel: FilteredViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val buyType = requireArguments().getString("buy_type")
        val estateType = requireArguments().getString("estate_type")
        val filterList = requireArguments().getSerializable("filter_set") as ArrayList<*>?
        val token = getToken(requireContext())
        if (buyType != null) {
            // send request to get all estate with this buy type
            viewModel.getEstateByBuyType(token, buyType)
                .observe(requireActivity(), this::onRealEstateChange)
        }
        if (estateType != null) {
            viewModel.getEstateByEstateType(token, estateType)
                .observe(requireActivity(), this::onRealEstateChange)
        }
        if (filterList != null) {
            val buyTypePair = filterList[0] as Pair<*,*>
            val estateTypePair = filterList[1] as Pair<*, *>
            val directionPair = filterList[2] as Pair<*, *>
            val priceRangePair = filterList[3] as Pair<*, *>

            val buyTypeField = buyTypePair.second.toString()

            val estateTypeField = estateTypePair.second.toString()

            val direction = directionPair.second.toString()

            val priceRange = priceRangePair.second as List<Float>
            viewModel.getEstateFullFilter(
                token,
                buyTypeField,
                estateTypeField,
                priceRange,
                direction
            )
                .observe(requireActivity(), this::onRealEstateChange)
        }

        return binding.root
    }

    private fun onRealEstateChange(estates: ArrayList<RealEstate>) {
        val adapter = RealEstateAdapter(estates)
        adapter.setItemClickListener(object : RealEstateItemClick<RealEstate> {
            override fun onClick(item: RealEstate) {
                val bundle = Bundle()
                bundle.putSerializable("estate", item)
                findNavController()
                    .navigate(R.id.action_filteredRealEstate_to_estateDetailsFragment, bundle)
            }

        })
        binding.rvRealEstate.adapter = adapter
    }
}