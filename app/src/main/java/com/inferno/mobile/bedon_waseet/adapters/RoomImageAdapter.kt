package com.inferno.mobile.bedon_waseet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.RoomImageItemBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstateRoomImages

class RoomImageAdapter(
    private val images: ArrayList<RealEstateRoomImages>,
    private val editable:Boolean
) : RecyclerView.Adapter<ImageHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        this.context = parent.context
        return ImageHolder(
            LayoutInflater.from(context).inflate(
                R.layout.room_image_item,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val url = if (images[position].id != -1)
            "${BaseApplication.BASE_URL}${images[position].url}"
        else images[position].url
        Glide.with(context)
            .load(url)
            .error(R.drawable.ic_baseline_error_outline_24)
            .placeholder(R.drawable.real_estate_logo)
            .into(holder.binding.image)
        if(editable){
            holder.binding.closeBtn.visibility = View.VISIBLE
            holder.binding.closeBtn.setOnClickListener {
                images.remove(images[holder.adapterPosition])
                notifyItemRemoved(holder.adapterPosition)
            }
        }else {
            holder.binding.closeBtn.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

}

class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = RoomImageItemBinding.bind(view)
}