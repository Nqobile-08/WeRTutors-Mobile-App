package com.ratjatji.eskhathinitutors.Admin

import com.ratjatji.eskhathinitutors.R


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ratjatji.eskhathinitutors.Tutors.JobApplication


class ApplicationAdapter(
    private val context: Context,
    private val applications: List<JobApplication>,
    private val onStatusChange: (JobApplication, String) -> Unit
) : RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>() {

    inner class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tutorName: TextView = itemView.findViewById(R.id.tutorNameTextView)
        val status: TextView = itemView.findViewById(R.id.statusTextView)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val rejectButton: Button = itemView.findViewById(R.id.rejectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false)
        return ApplicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val application = applications[position]
        holder.tutorName.text = application.tutorName
        holder.status.text = "Status: ${application.status}"

        holder.acceptButton.setOnClickListener {
            onStatusChange(application, "Accepted")
        }

        holder.rejectButton.setOnClickListener {
            onStatusChange(application, "Rejected")
        }
    }

    override fun getItemCount(): Int = applications.size
}
