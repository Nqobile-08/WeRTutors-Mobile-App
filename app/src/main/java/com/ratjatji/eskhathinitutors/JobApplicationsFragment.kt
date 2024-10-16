package com.ratjatji.eskhathinitutors.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.Tutors.Job
import com.ratjatji.eskhathinitutors.Tutors.JobApplication

class JobApplicationsFragment : Fragment() {

    private lateinit var applicationRecyclerView: RecyclerView
    private lateinit var applicationAdapter: ApplicationAdapter
    private lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_job_applications, container, false)
        applicationRecyclerView = view.findViewById(R.id.applicationRecyclerView)

        // Set up the RecyclerView
        applicationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        applicationAdapter = ApplicationAdapter(requireContext(), job.applications) { application, status ->
            updateApplicationStatus(application, status)
        }
        applicationRecyclerView.adapter = applicationAdapter

        return view
    }

    private fun updateApplicationStatus(application: JobApplication, status: String) {
        // Update the application status (Pending -> Accepted/Rejected)
        application.status = status
        applicationAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(job: Job): JobApplicationsFragment {
            val fragment = JobApplicationsFragment()
            fragment.job = job
            return fragment
        }
    }
}
