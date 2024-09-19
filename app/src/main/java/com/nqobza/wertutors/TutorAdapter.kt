package com.nqobza.wertutors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.nqobza.wertutors.R
import com.nqobza.wertutors.Tutor
import com.nqobza.wertutors.databinding.ItemTutorBinding

class TutorAdapter(   private var tutorsList: ArrayList<Tutor>) :
    RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {

        val itemView =LayoutInflater.from(parent.context).inflate(
            R.layout.item_tutor,
            parent,false)
        return TutorViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tutorsList.size
    }

    override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
        val currentTutor = tutorsList[position]
        holder.rvProfilePic.setImageResource(currentTutor.ProfilePic)
        holder.rvTutorName.text = currentTutor.Name
        holder.rvRating.text = String.format("%.1f", currentTutor.Rating)
        holder.rvRate.text = String.format("%.2f", currentTutor.Rate)
        holder.rvLocation.text = currentTutor.Location
        holder.rvLanguage.text = currentTutor.Language
        holder.rvDescription.text = currentTutor.Description

    }
    class TutorViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val rvProfilePic : ImageView = itemView.findViewById(R.id.ivProfilePic)
        val rvTutorName : TextView = itemView.findViewById(R.id.tvTutorName)
        val rvRating : TextView = itemView.findViewById(R.id.tvRating)
        val rvRate : TextView = itemView.findViewById(R.id.tvRate)
        val rvLocation : TextView = itemView.findViewById(R.id.tvLocation)


        val rvLanguage : TextView = itemView.findViewById(R.id.tvLanguage)
        val rvDescription : TextView = itemView.findViewById(R.id.tvDescription)
    }
}
