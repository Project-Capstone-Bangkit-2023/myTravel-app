package com.capstoneproject.mytravel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.R

class  ReviewsAdapter(private val listReview: List<ReviewUser>) : RecyclerView.Adapter<ReviewsAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback() {

    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_user)
        val tvName: TextView = itemView.findViewById(R.id.tv_user)
        val tvReview: TextView = itemView.findViewById(R.id.tv_review)
        val imgOne: ImageView = itemView.findViewById(R.id.img_one_star)
        val imgTwo: ImageView = itemView.findViewById(R.id.img_two_star)
        val imgThree: ImageView = itemView.findViewById(R.id.img_three_star)
        val imgFour: ImageView = itemView.findViewById(R.id.img_four_star)
        val imgFive: ImageView = itemView.findViewById(R.id.img_five_star)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_reviews, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listReview.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, review, rating) = listReview[position]

        holder.tvName.text = name
        holder.tvReview.text = review
        if (rating == 1) {
            holder.imgOne.visibility = View.VISIBLE
            holder.imgTwo.visibility = View.GONE
            holder.imgThree.visibility = View.GONE
            holder.imgFour.visibility = View.GONE
            holder.imgFive.visibility = View.GONE
        }else if(rating == 2){
            holder.imgOne.visibility = View.VISIBLE
            holder.imgTwo.visibility = View.VISIBLE
            holder.imgThree.visibility = View.GONE
            holder.imgFour.visibility = View.GONE
            holder.imgFive.visibility = View.GONE
        }else if(rating == 3){
            holder.imgOne.visibility = View.VISIBLE
            holder.imgTwo.visibility = View.VISIBLE
            holder.imgThree.visibility = View.VISIBLE
            holder.imgFour.visibility = View.GONE
            holder.imgFive.visibility = View.GONE
        }else if(rating == 4){
            holder.imgOne.visibility = View.VISIBLE
            holder.imgTwo.visibility = View.VISIBLE
            holder.imgThree.visibility = View.VISIBLE
            holder.imgFour.visibility = View.VISIBLE
            holder.imgFive.visibility = View.GONE
        }else{
            holder.imgOne.visibility = View.VISIBLE
            holder.imgTwo.visibility = View.VISIBLE
            holder.imgThree.visibility = View.VISIBLE
            holder.imgFour.visibility = View.VISIBLE
            holder.imgFive.visibility = View.VISIBLE
        }

    }
    interface OnItemClickCallback {
        fun onItemClicked(data: ReviewUser)
    }
}