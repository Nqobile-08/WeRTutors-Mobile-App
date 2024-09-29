package com.nqobza.wertutors

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ReviewAdapter(private val reviewList: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]

        // Bind data to the TextViews
        holder.tutorRating.text = "Rating: ${review.rating ?: "N/A"}"
        holder.reviewSubject.text = review.subject ?: "No subject provided"
        holder.reviewDescription.text = review.description ?: "No description provided"
        holder.studentName.text = "By ${review.studentName ?: "Anonymous"}"
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tutorRating: TextView = itemView.findViewById(R.id.tvTutorRating)
        val reviewSubject: TextView = itemView.findViewById(R.id.tvReviewSubject)
        val reviewDescription: TextView = itemView.findViewById(R.id.tvReviewDescription)
        val studentName: TextView = itemView.findViewById(R.id.tvStudentName)
    }
}
