package com.inferno.mobile.bedon_waseet.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.inferno.mobile.bedon_waseet.BaseApplication
import com.inferno.mobile.bedon_waseet.R
import com.inferno.mobile.bedon_waseet.databinding.AddButtonItemBinding
import com.inferno.mobile.bedon_waseet.databinding.RoomItemBinding
import com.inferno.mobile.bedon_waseet.responses.RealEstate
import com.inferno.mobile.bedon_waseet.responses.RealEstateRoomImages
import com.inferno.mobile.bedon_waseet.responses.RealEstateRooms


interface OnAddRoomClickListener {
    fun onClick(id: Int)
}

interface OnSendToServer {
    fun onLongClick(roomName: String, images: ArrayList<RealEstateRoomImages>)
}

class EditRoomAdapter(

    private val rooms: ArrayList<RealEstateRooms>
) : RecyclerView.Adapter<EditImagesHolder>() {
    private lateinit var context: Context
    private var listener: OnAddRoomClickListener? = null
    private var longListener: OnSendToServer? = null
    fun setOnAddRoomClickListener(listener: OnAddRoomClickListener) {
        this.listener = listener
    }

    fun setOnSendClickListener(listener: OnSendToServer) {
        this.longListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImagesHolder {
        this.context = parent.context
        return EditImagesHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.room_item, parent, false)
        )

    }

    fun addRoom(room: RealEstateRooms) {
        rooms.add(room)
        notifyItemInserted(rooms.size)
    }


    fun addImages(images: ArrayList<RealEstateRoomImages>, pos: Int) {
        rooms[pos].images!!.addAll(images)
        notifyItemChanged(pos)
    }

    override fun onBindViewHolder(holder: EditImagesHolder, position: Int) {
        val room = rooms[position]
        holder.binding.roomName.text = room.name
        val adapter = RoomImageAdapter(room.images!!,true)
        holder.binding.imageSlider.adapter = adapter
        holder.binding.addButton.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(holder.adapterPosition)

            }
        }
        holder.binding.addButton.setOnLongClickListener {
            if (longListener != null)
                longListener!!.onLongClick(
                    rooms[holder.adapterPosition].name,
                    rooms[holder.adapterPosition].images!!
                )
            false
        }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }
}

class EditImagesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = RoomItemBinding.bind(itemView)
}
