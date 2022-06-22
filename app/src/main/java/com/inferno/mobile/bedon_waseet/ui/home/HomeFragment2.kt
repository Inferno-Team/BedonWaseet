package com.inferno.mobile.bedon_waseet.ui.home

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.RangeSlider
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.adapters.RealEstateAdapter
import com.inferno.mobile.bedon_waseet.adapters.RealEstateItemClick
import com.inferno.mobile.bedon_waseet.adapters.TypesAdapter
import com.inferno.mobile.bedon_waseet.adapters.WhatAdapter
import com.inferno.mobile.bedon_waseet.databinding.FilterLayoutBinding
import com.inferno.mobile.bedon_waseet.databinding.MainHomeLayoutBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.BuyType
import com.inferno.mobile.bedon_waseet.utils.DirectionType
import com.inferno.mobile.bedon_waseet.utils.RealEstateType
import com.inferno.mobile.bedon_waseet.utils.getToken
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment2 : Fragment() {
    lateinit var binding: MainHomeLayoutBinding
    val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainHomeLayoutBinding.inflate(inflater, container, false)
        val token = getToken(requireContext())
        binding.root.setOnRefreshListener {
            binding.recentRv.swapAdapter(null, true)
            viewModel.getRecentRealEstate(token).observe(requireActivity(), this::recentRealEstate)
        }

        viewModel.getRecentRealEstate(token).observe(requireActivity(), this::recentRealEstate)
//        viewModel.getRealEstateTypes(token).observe(requireActivity(),this::realEstateTypes)
        val types = ArrayList<RealEstateType>()
        types.add(RealEstateType.محل)
        types.add(RealEstateType.ارض)
        types.add(RealEstateType.شقة)
        types.add(RealEstateType.منزل)
        realEstateTypes(types)

        val buyTypes = ArrayList<BuyType>()
        buyTypes.add(BuyType.رهن)
        buyTypes.add(BuyType.استأجار)
        buyTypes.add(BuyType.بيع)
        realEstateBuyTypes(buyTypes)
        binding.filterShower.setOnClickListener {
            showFilterDialog()
        }
        return binding.root
    }

    private fun showFilterDialog() {
        val dialog = BottomSheetDialog(requireActivity())
        val dialogBinding = FilterLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val estateOptions = listOf(
            RealEstateType.منزل.name,
            RealEstateType.شقة.name,
            RealEstateType.ارض.name,
            RealEstateType.محل.name,
        )
        val directionOptions = listOf(
            DirectionType.شمال.name,
            DirectionType.جنوب.name,
            DirectionType.غرب.name,
            DirectionType.شرق.name
        )
        val typeOptions = listOf(
            BuyType.بيع.name,
            BuyType.استأجار.name,
            BuyType.رهن.name,
        )
        val typeAdapter = ArrayAdapter(requireContext(),
            R.layout.list_item, estateOptions)
        val directionAdapter = ArrayAdapter(requireContext(), R.layout.list_item, directionOptions)
        val buyAdapter = ArrayAdapter(requireContext(), R.layout.list_item, typeOptions)
        dialogBinding.directionField.setAdapter(directionAdapter)
        dialogBinding.buyTypeField.setAdapter(buyAdapter)
        dialogBinding.typeField.setAdapter(typeAdapter)
        dialogBinding.typeField.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                dataChanged(dialogBinding)
            }
        dialogBinding.buyTypeField.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                dataChanged(dialogBinding)
            }
        dialogBinding.directionField.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                dataChanged(dialogBinding)
            }

        dialogBinding.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.priceSlider.addOnChangeListener { rangeSlider, value, fromUser ->

            if (fromUser) {
                val values = dialogBinding.priceSlider.values
                val valuesString = convertValues(values)
                dialogBinding.priceField.text = valuesString
                dialogBinding.clearBtn.visibility = View.VISIBLE
            } else {
                dialogBinding.priceField.text = ""
            }
        }

        dialogBinding.priceSlider.valueFrom = 0.0F
        dialogBinding.priceSlider.valueTo = 6e6F
        dialogBinding.priceSlider.values = listOf(0.0F, 0.0F)
        dialogBinding.priceSlider.stepSize = 1e3F
        dialogBinding.clearBtn.setOnClickListener {
            dialogBinding.typeField.setText("", false)
            dialogBinding.directionField.setText("", false)
            dialogBinding.buyTypeField.setText("", false)
            dialogBinding.priceSlider.values = listOf(.0F, .0F)
            dialogBinding.clearBtn.visibility = View.INVISIBLE
        }
        dialogBinding.applyFilter.setOnClickListener {
            val buyType = dialogBinding.buyTypeField.text.toString()
            val estateType = dialogBinding.typeField.text.toString()
            val direction = dialogBinding.directionField.text.toString()
            val priceRange = dialogBinding.priceSlider.values
            val filterList = ArrayList<Pair<String, Any>>()
            filterList.add(Pair("buy_type", buyType))
            filterList.add(Pair("estate_type", estateType))
            filterList.add(Pair("direction", direction))
            filterList.add(Pair("priceRange", priceRange))
            val bundle = Bundle()
            bundle.putSerializable("filter_set", filterList)
            findNavController().navigate(R.id.action_homeFragment2_to_filteredRealEstate, bundle)
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun convertValues(values: List<Float>): String {
        val first = values[1]
        val second = values[0]
        val string = StringBuilder()
        if (first > 1e3F) {
            // 300,000 => 300k
            // 300,000 % 1e3 = 300
            if (first > 1e6F) {
                // 300,000,000 => 300M
                // 300,000 % 1e6 = 300
                string.append(first / 1e6F).append("مليون ل.س")
            } else
                string.append(first / 1e3F).append("ألف ل.س")
        }
        string.append("\n")
        if (second > 1e3F) {
            // 300,000 => 300k
            // 300,000 % 1e3 = 300
            if (second > 1e6F) {
                // 300,000,000 => 300M
                // 300,000 % 1e6 = 300
                string.append(second / 1e6F).append("مليون ل.س")
            } else
                string.append(second / 1e3F).append("ألف ل.س")
        }
        return string.toString()
    }

    private fun dataChanged(binding: FilterLayoutBinding) {
        if (
            binding.typeField.text.toString() != "" ||
            binding.buyTypeField.text.toString() != "" ||
            binding.directionField.text.toString() != ""
        ) {
            binding.clearBtn.visibility = View.VISIBLE
        }
    }

    private fun realEstateTypes(types: ArrayList<RealEstateType>) {
        val adapter = TypesAdapter(types)
        adapter.setOnTypeClickListener(object : RealEstateItemClick<RealEstateType> {
            override fun onClick(item: RealEstateType) {
                val bundle = Bundle()
                bundle.putString("estate_type", item.name)
                findNavController().navigate(
                    R.id.action_homeFragment2_to_filteredRealEstate,
                    bundle
                )

            }

        })
        binding.realStateTypes.adapter = adapter
    }

    private fun realEstateBuyTypes(types: ArrayList<BuyType>) {
        val adapter = WhatAdapter(types)
        adapter.setOnTypeClickListener(object : RealEstateItemClick<BuyType> {
            override fun onClick(item: BuyType) {
                val bundle = Bundle()
                bundle.putString("buy_type", item.name)
                findNavController().navigate(
                    R.id.action_homeFragment2_to_filteredRealEstate,
                    bundle
                )
            }

        })
        binding.whatRv.adapter = adapter
    }

    private fun recentRealEstate(estates: ArrayList<RealEstate>) {
        binding.root.isRefreshing = false
        val adapter = RealEstateAdapter(estates)
        adapter.setItemClickListener(object : RealEstateItemClick<RealEstate> {
            override fun onClick(item: RealEstate) {
                val controller = findNavController()
                val bundle = Bundle()
                bundle.putSerializable("estate", item)
                controller.navigate(R.id.action_homeFragment2_to_estateDetailsFragment, bundle)
            }

        })
        binding.recentRv.adapter = adapter
    }
}