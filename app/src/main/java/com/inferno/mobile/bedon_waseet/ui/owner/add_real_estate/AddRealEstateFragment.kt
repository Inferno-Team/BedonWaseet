package com.inferno.mobile.bedon_waseet.ui.owner.add_real_estate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.AddRealEstateFragmentBinding
import com.inferno.mobile.bedon_waseet.responses.MessageResponse
import com.inferno.mobile.bedon_waseet.ui.owner.my_realestates.MyEstateViewModel
import com.inferno.mobile.bedon_waseet.utils.BuyType
import com.inferno.mobile.bedon_waseet.utils.DirectionType
import com.inferno.mobile.bedon_waseet.utils.RealEstateType
import com.inferno.mobile.bedon_waseet.utils.getToken

class AddRealEstateFragment : Fragment() {
    private lateinit var binding: AddRealEstateFragmentBinding
    private val viewModel: MyEstateViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddRealEstateFragmentBinding.inflate(inflater, container, false)

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
        val estateOptions = listOf(
            RealEstateType.منزل.name,
            RealEstateType.شقة.name,
            RealEstateType.ارض.name,
            RealEstateType.محل.name,
        )

        val typeAdapter = ArrayAdapter(requireContext(), R.layout.list_item, estateOptions)
        val directionAdapter = ArrayAdapter(requireContext(), R.layout.list_item, directionOptions)
        val buyAdapter = ArrayAdapter(requireContext(), R.layout.list_item, typeOptions)

        binding.typeField.setAdapter(typeAdapter)
        binding.directionField.setAdapter(directionAdapter)
        binding.buyTypeField.setAdapter(buyAdapter)

        binding.addBtn.setOnClickListener {
            val token = getToken(requireContext())
            val location = binding.locationField.text.toString()
            val direction = binding.directionField.text.toString()
            val type = binding.typeField.text.toString()
            val buyType = binding.buyTypeField.text.toString()
            val _long = binding.longField.text.toString().toDouble()
            val lat = binding.latField.text.toString().toDouble()
            val price = binding.priceField.text.toString().toDouble()
            val area = binding.areaField.text.toString().toDouble()
            viewModel.addRealEstate(
                token, location, direction, type,
                _long, lat, price, buyType, area
            ).observe(requireActivity(), this::onMessage)
        }

        return binding.root
    }

    private fun onMessage(response: MessageResponse) {
        if (response.code == 200) {
            findNavController().navigateUp()
        }
//        Toast.makeText(context, response.msg, Toast.LENGTH_LONG).show()
    }
}