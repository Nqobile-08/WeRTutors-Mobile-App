package com.ratjatji.eskhathinitutors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ratjatji.eskhathinitutors.Tutors.Job

class JobAdapter(
    private val context: Context,
    private val jobs: List<Job>,
    private val onApplyClick: (Job) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.studentNameTextView)
        val grade: TextView = itemView.findViewById(R.id.gradeTextView)
        val applyButton: Button = itemView.findViewById(R.id.applyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.studentName.text = job.studentName
        holder.grade.text = "Grade: ${job.grade}"
        holder.applyButton.setOnClickListener {
            onApplyClick(job)
        }
    }

    override fun getItemCount(): Int = jobs.size
}
