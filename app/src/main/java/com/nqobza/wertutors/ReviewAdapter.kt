package com.nqobza.wertutors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ReviewAdapter(private var reviewList: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ReviewAdapter.ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_review,parent,false)
        return ReviewViewHolder(itemView)
        }

    override fun getItemCount(): Int {
       return reviewList.size
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        val currentReview = reviewList[position]
        holder.reviewTutorName.text = currentReview.tutorName
        holder.reviewStudentName.text = currentReview.studentName
        holder.reviewRating.text = currentReview.rating.toString()
        holder.reviewSubject.text = currentReview.subject
        holder.reviewDescription.text = currentReview.description
    }
    class ReviewViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        val reviewRating : TextView = itemView.findViewById(R.id.tvTutorRating)
        val reviewTutorName : TextView = itemView.findViewById(R.id.tvTutorName)
        val reviewStudentName : TextView = itemView.findViewById(R.id.tvStudentName)
        val reviewSubject : TextView = itemView.findViewById(R.id.tvReviewSubject)
        val reviewDescription : TextView = itemView.findViewById(R.id.tvReviewDescription)
    }
}