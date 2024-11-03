package com.ratjatji.eskhathinitutors.Tutors

data class Job(
 val jobId: String,
 val studentName: String,
 val grade: Int,
 val rate: Double,
 val type: String,
 val school: String,
 val address: String,
 val subjectList: List<String>,
 val applications: MutableList<JobApplication> = mutableListOf()

)

data class JobApplication(
 val tutorName: String,
 val applicationId: String,
 val jobId: String,
 val tutorId: String,
 var status: String // "Pending", "Accepted", "Rejected"
)

