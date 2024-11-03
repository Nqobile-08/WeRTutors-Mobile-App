package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ratjatji.eskhathinitutors.Tutors.Job


class JobsFragment : Fragment() {

    private lateinit var jobRecyclerView: RecyclerView
    private lateinit var jobAdapter: JobAdapter
    private val jobList = mutableListOf<Job>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_jobs, container, false)
        jobRecyclerView = view.findViewById(R.id.jobs_RecyclerView)

        // Set up the RecyclerView
        jobRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(requireContext(), jobList) { job ->
            applyForJob(job)
        }
        jobRecyclerView.adapter = jobAdapter

        // Load the job list
        loadJobs()

        return view
    }

    private fun loadJobs() {
        // Load job data (this would be from your backend in a real app)
        jobList.add(Job("1", "Amosego Mavimbela", 8, 100.0, "Online", "N/A", "N/A", listOf("Maths", "IsiZulu")))
        jobList.add(Job("2", "Priscilla Mchaonyerwa", 9, 150.0, "In-Person", "Curro Savannah", "Address XYZ", listOf("English")))
        jobAdapter.notifyDataSetChanged()
    }

    private fun applyForJob(job: Job) {
        // Logic to apply for the job (e.g., save to database)
    }
}
