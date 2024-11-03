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

class AdminJobsFragment : Fragment() {

    private lateinit var adminJobRecyclerView: RecyclerView
    private lateinit var adminJobAdapter: AdminJobAdapter
    private val jobList = mutableListOf<Job>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_jobs, container, false)
        adminJobRecyclerView = view.findViewById(R.id.adminJobRecyclerView)

        // Set up the RecyclerView
        adminJobRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adminJobAdapter = AdminJobAdapter(requireContext(), jobList) { job ->
            openJobApplications(job)
        }
        adminJobRecyclerView.adapter = adminJobAdapter

        loadJobs()

        return view
    }

    private fun loadJobs() {
        // Load jobs with applications (placeholder function, replace with actual DB call)
        val job1 = Job("1", "Amosego Mavimbela", 8, 100.0, "Online", "N/A", "N/A", listOf("Maths", "IsiZulu"))
        job1.applications.add(JobApplication("app_1", "1", "tutor_1", "Tutor A", "Pending"))
        job1.applications.add(JobApplication("app_2", "1", "tutor_2", "Tutor B", "Pending"))
        jobList.add(job1)

        jobList.add(Job("2", "Priscilla Mchaonyerwa", 9, 150.0, "In-Person", "Curro Savannah", "Address XYZ", listOf("English")))
        adminJobAdapter.notifyDataSetChanged()
    }

    private fun openJobApplications(job: Job) {
        // Navigate to a new fragment to view and approve/reject applications for the selected job
        val fragment = JobApplicationsFragment.newInstance(job)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
