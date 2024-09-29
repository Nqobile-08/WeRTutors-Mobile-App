package com.nqobza.wertutors

import java.io.Serializable

data class Tutor(
    var ProfilePic: Int,
    var Name: String,
    var Rating: Double,
    var Rate: Double,
    var Location: String,
    var Language: String,
    var Description: String,
    var Subjects: String,
    var WorkExperience: String,
    var Education: String,
    var CoursesCertifications: String,
    var Skills: String,
    var Levels: String

    ): Serializable
