package com.inferno.mobile.bedon_waseet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.WhatLookingForItemBinding
import com.inferno.mobile.bedon_waseet.utils.BuyType
import com.inferno.mobile.bedon_waseet.utils.RealEstateType

class WhatAdapter(
    private val whats:ArrayList<BuyType>
): RecyclerView.Adapter<WhatViewHolder>() {

    private var onTypeClickListener: RealEstateItemClick<BuyType>? = null

    fun setOnTypeClickListener(onTypeClickListener: RealEstateItemClick<BuyType>) {
        this.onTypeClickListener = onTypeClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhatViewHolder {
        return WhatViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.what_looking_for_item,parent,false))
    }

    override fun onBindViewHolder(holder: WhatViewHolder, position: Int) {
        val what = whats[position]
        holder.binding.name.text = what.name
        holder.itemView.setOnClickListener{
            if(onTypeClickListener!=null){
                onTypeClickListener!!.onClick(what)
            }
        }

    }

    override fun getItemCount(): Int {
        return whats.size
    }
}
class WhatViewHolder(v:View):RecyclerView.ViewHolder(v){
    val binding = WhatLookingForItemBinding.bind(v)
}