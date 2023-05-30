package com.capstoneproject.mytravel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.R

class  ReviewsAdapter(private val listRecommend: List<Recommend>) : RecyclerView.Adapter<ReviewsAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_user)
        val tvName: TextView = itemView.findViewById(R.id.tv_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_reviews, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listRecommend.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, photo) = listRecommend[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.imgPhoto)
        holder.tvName.text = username
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listRecommend[holder.adapterPosition])
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: Recommend)
    }
}