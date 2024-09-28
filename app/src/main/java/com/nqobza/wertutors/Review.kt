package com.nqobza.wertutors

data class Review(
    var reviewID: String,
    var tutorName: String,
    var rating: Int,
    var studentName: String,
    var subject: String,
    var description: String,
)
