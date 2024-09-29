package com.nqobza.wertutors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TutorAdapter(private var tutorsList: ArrayList<Tutor>) :
    RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_tutor,
            parent, false
        )
        return TutorViewHolder(itemView, mListener, tutorsList)
    }

    override fun getItemCount(): Int {
        return tutorsList.size
    }

    override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
        val currentTutor = tutorsList[position]
        holder.rvProfilePic.setImageResource(currentTutor.ProfilePic)
        holder.rvTutorName.text = currentTutor.Name
        holder.rvRating.text = currentTutor.Rating.toString()
        holder.rvRate.text = "R ${currentTutor.Rate}/ hour"
        holder.rvLocation.text = currentTutor.Location
        holder.rvLanguages.text = currentTutor.Language
        holder.rvDescription.text = currentTutor.Description
        holder.rvSubject.text = currentTutor.Subjects.toString()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, TutorExpandedActivity::class.java)
            intent.putExtra("TUTOR_NAME", currentTutor.Name)
            intent.putExtra("ProfilePic", currentTutor.ProfilePic)
            intent.putExtra("Rating", currentTutor.Rating)
            intent.putExtra("Rate", currentTutor.Rate)
            intent.putExtra("Location", currentTutor.Location)
            intent.putExtra("Language", currentTutor.Language)
            intent.putExtra("Description", currentTutor.Description)
            intent.putExtra("WorkExperience", currentTutor.WorkExperience)
            intent.putExtra("Education", currentTutor.Education)
            intent.putExtra("CoursesCertifications", currentTutor.CoursesCertifications)
            intent.putExtra("Skills", currentTutor.Skills)
            intent.putExtra("Subjects", currentTutor.Subjects) // Pass subjects as an array
            context.startActivity(intent)
        }
    }

    class TutorViewHolder(itemView: View, listener: onItemClickListener, tutorsList: ArrayList<Tutor>) :
        RecyclerView.ViewHolder(itemView) {
        val rvProfilePic: ImageView = itemView.findViewById(R.id.ivProfilePic)
        val rvTutorName: TextView = itemView.findViewById(R.id.tvTutorName)
        val rvRating: TextView = itemView.findViewById(R.id.tvRating)
        val rvRate: TextView = itemView.findViewById(R.id.tvRate)
        val rvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val rvLanguages: TextView = itemView.findViewById(R.id.tvLanguage)
        val rvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val rvSubject: TextView = itemView.findViewById(R.id.tvSubjects)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
