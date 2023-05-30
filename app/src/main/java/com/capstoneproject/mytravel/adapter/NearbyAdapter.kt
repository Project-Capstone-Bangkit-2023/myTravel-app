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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_nearby, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listNearby.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, photo) = listNearby[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.imgPhoto)
        holder.tvName.text = username
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listNearby[holder.adapterPosition])
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: Nearby)
    }
}