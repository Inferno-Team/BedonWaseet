package com.inferno.mobile.bedon_waseet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.RoomItemBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstateRooms

class RealEstateRoomAdapter(
    private val rooms: List<RealEstateRooms>,
    private val editable:Boolean
) : RecyclerView.Adapter<ImagesHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
        this.context = parent.context
        return ImagesHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.room_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {

        val room = rooms[position]
        holder.binding.roomName.text = room.name

        val adapter = RoomImageAdapter(room.images!!,editable)
        holder.binding.imageSlider.adapter = adapter
        if(!editable)
            holder.binding.addButton.visibility= View.GONE
        else holder.binding.addButton.visibility= View.VISIBLE
    }

    override fun getItemCount(): Int {
        return rooms.size
    }
}

class ImagesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: RoomItemBinding = RoomItemBinding.bind(itemView)
}