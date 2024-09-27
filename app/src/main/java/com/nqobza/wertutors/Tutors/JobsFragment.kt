package com.nqobza.wertutors.Tutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nqobza.wertutors.R

import com.nqobza.wetutors.JobsAdapter


class JobsFragment : Fragment() {

    private val jobsViewModel: JobsViewModel by viewModels()
private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jobs, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.jobs_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        jobsViewModel.jobs.observe(viewLifecycleOwner, Observer { jobs ->
            recyclerView.adapter = JobsAdapter(jobs)
        })

        return view
    }
}
