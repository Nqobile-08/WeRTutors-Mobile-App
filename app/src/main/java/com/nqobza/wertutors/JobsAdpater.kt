package com.nqobza.wetutors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nqobza.wertutors.databinding.ItemJobBinding

class JobsAdapter(private val jobs: List<Job>) : RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    class JobViewHolder(private val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: Job) {
            binding.jobTitle.text = job.title
            binding.jobDescription.text = job.description
            binding.studentName.text = job.studentName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(jobs[position])
    }

    override fun getItemCount() = jobs.size
}
