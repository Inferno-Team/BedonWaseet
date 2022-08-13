package com.inferno.mobile.bedon_waseet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.RealEstateTypeItemBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.utils.RealEstateType


class TypesAdapter(
    private val types: ArrayList<Pair<String, RealEstateType>>
) : RecyclerView.Adapter<TypeViewHolder>() {
    private var onTypeClickListener: RealEstateItemClick<Pair<String, RealEstateType>>? = null

    fun setOnTypeClickListener(
        onTypeClickListener:
        RealEstateItemClick<Pair<String, RealEstateType>>
    ) {
        this.onTypeClickListener = onTypeClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        return TypeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.real_estate_type_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = types[position]
        holder.binding.typeName.text = type.second.name
        Glide.with(holder.binding.root.context)
            .load(type.first)
            .centerCrop()
            .placeholder(
                AppCompatResources.getDrawable(
                    holder.itemView.context, R.drawable.buy
                )
            )
            .into(holder.binding.typeImage)
        holder.itemView.setOnClickListener {
            onTypeClickListener?.onClick(type)
        }
    }

    override fun getItemCount(): Int {
        return types.size
    }
}

class TypeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val binding = RealEstateTypeItemBinding.bind(v)

}