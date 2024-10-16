package com.ratjatji.eskhathinitutors.Admin

import com.ratjatji.eskhathinitutors.R



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ratjatji.eskhathinitutors.Tutors.Job


class AdminJobAdapter(
    private val context: Context,
    private val jobs: List<Job>,
    private val onViewApplicationsClick: (Job) -> Unit
) : RecyclerView.Adapter<AdminJobAdapter.AdminJobViewHolder>() {

    inner class AdminJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.studentNameTextView)
        val grade: TextView = itemView.findViewById(R.id.gradeTextView)
        val viewApplicationsButton: Button = itemView.findViewById(R.id.viewApplicationsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminJobViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_admin_job, parent, false)
        return AdminJobViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminJobViewHolder, position: Int) {
        val job = jobs[position]
        holder.studentName.text = job.studentName
        holder.grade.text = "Grade: ${job.grade}"
        holder.viewApplicationsButton.setOnClickListener {
            onViewApplicationsClick(job)
        }
    }

    override fun getItemCount(): Int = jobs.size
}
