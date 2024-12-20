package com.ratjatji.eskhathinitutors
data class Review(
    var reviewID: String? = null,
    var tutorName: String? = null,
    var rating: Int? = null,
    var studentName: String? = null,
    var subject: String? = null,
    var description: String? = null,
    val goodRemarks: List<String> = listOf(), // Add this field
    val badRemarks: List<String> = listOf()
) {
    // No-argument constructor required by Firebase
    constructor() : this(null, null, null, null, null, null)
}
