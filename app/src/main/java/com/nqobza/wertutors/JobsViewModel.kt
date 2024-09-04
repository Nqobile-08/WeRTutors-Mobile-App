package com.nqobza.wetutors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JobsViewModel : ViewModel() {
    private val _jobs = MutableLiveData<List<Job>>()
    val jobs: LiveData<List<Job>> get() = _jobs

    init {
        loadJobs()
    }

    private fun loadJobs() {
        // Simulate loading jobs from a data source
        _jobs.value = listOf(
            Job("1", "Math Tutoring", "Help with algebra and geometry.", "John Doe"),
            Job("2", "Science Project", "Assist with a science project.", "Jane Smith"),
            Job("3", "Hitory", "History", "JordaSmith"),
                    Job("4", "English", "needs help in english", "Desmond Silva"),

            // Add more jobs as needed
        )
    }
}
