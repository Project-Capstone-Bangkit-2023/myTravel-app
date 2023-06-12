package com.capstoneproject.mytravel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.R

class NearbyAdapter(private val listNearby: List<Nearby>) : RecyclerView.Adapter<NearbyAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val tvWeather: TextView = itemView.findViewById(R.id.tvWeather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_nearby, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listNearby.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, name, category, photo, city, rating, price, desc, lat, lon, distance) = listNearby[position]
        val photoUrl = "https://storage.googleapis.com/mytravel_bucket/places/$photo"
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .into(holder.imgPhoto)
        holder.tvName.text = name
        holder.tvAddress.text = city
        val formatDistance = String.format("%.1f", distance)
        holder.tvDistance.text = "$formatDistance km"
        holder.tvWeather.text = "0.0"
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listNearby[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Nearby)
    }
}