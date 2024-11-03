package com.ratjatji.eskhathinitutors

import java.io.Serializable

data class Tutors1(
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
    var Levels: String,
    var RatingCount: Int= 0,
): Serializable
