package com.inferno.mobile.bedon_waseet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.RealEstateItemBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate


interface RealEstateItemClick<T> {
    fun onClick(item: T)
}


class RealEstateAdapter(
    private val estates: ArrayList<RealEstate>
) : RecyclerView.Adapter<EstateHolder>() {
    private lateinit var realEstateItemClickListener: RealEstateItemClick<RealEstate>
    private lateinit var context: Context
    fun setItemClickListener(l: RealEstateItemClick<RealEstate>) {
        realEstateItemClickListener=l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateHolder {
        this.context = parent.context
        return EstateHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.real_estate_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EstateHolder, position: Int) {
        val estate = estates[position]
        if (estate.image != null){
            val url = if(estate.image.startsWith("http"))
                estate.image else BaseApplication.BASE_URL + estate.image
            Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(AppCompatResources.getDrawable(context, R.drawable.real_estate_logo))
                .into(holder.binding.imgRealEstate)
        }

        holder.binding.estate = estate
        holder.binding.root.setOnClickListener {
            realEstateItemClickListener.onClick(estate)
        }


    }

    override fun getItemCount(): Int {
        return estates.size
    }
}

class EstateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: RealEstateItemBinding = RealEstateItemBinding.bind(itemView)
}