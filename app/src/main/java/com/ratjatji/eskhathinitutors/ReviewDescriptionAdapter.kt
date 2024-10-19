package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class  ReviewDescriptionAdapter(private var BadReviewList: ArrayList<BadReview>):
    RecyclerView.Adapter<ReviewDescriptionAdapter.BadReviewViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_bad_review,
            parent, false
        )
        return BadReviewViewHolder(itemView, mListener, BadReviewList)
    }

    override fun getItemCount(): Int {
        return BadReviewList.size
    }

    override fun onBindViewHolder(holder: BadReviewViewHolder, position: Int) {
        val currentBadReview = BadReviewList[position]
        holder.rvDescription.text = currentBadReview.ReviewText
    }

    class BadReviewViewHolder(itemView: View, listener: onItemClickListener, BadReviewList: ArrayList<BadReview>) :
        RecyclerView.ViewHolder(itemView) {
        val rvDescription: TextView = itemView.findViewById(R.id.tvBadReviewDescription)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    }