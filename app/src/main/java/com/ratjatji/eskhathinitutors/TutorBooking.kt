package com.ratjatji.eskhathinitutors

data class TutorBooking(
    val name: String,
    val subjects: List<String>,
    val availableDays: List<Int>,  // 1: Sunday, 2: Monday, ..., 7: Saturday
    val availableTimeSlots: List<String>  // Time slots, e.g., "12:00 - 13:00"
)

val tutorOptions = listOf(
    TutorBooking("Sipho Mthethwa", listOf("Algebra", "Trigonometry", "Geometry", "Calculus"), listOf(7, 1), listOf("12:00 - 13:00", "13:00 - 14:00")),
    TutorBooking("Zanele Mhlongo", listOf("Business Management", "Economics", "Statistics"), listOf(2, 3, 4), listOf("10:00 - 12:00")),
    TutorBooking("Kabelo Phiri", listOf("Python", "C++", "Java", "Algorithms"), listOf(5, 6), listOf("15:00 - 16:00", "16:00 - 17:00")),
    TutorBooking("Lerato Mokwoena", listOf("Data Science", "Machine Learning", "Statistics", "Big Data"), listOf(2, 4, 6), listOf("09:00 - 10:00")),
    TutorBooking("Sizwe Nkosi", listOf("Mathematics", "Physics", "Engineering Mechanics"), listOf(3, 5), listOf("13:00 - 14:00", "14:00 - 15:00")),
    TutorBooking("Mbali Mkhize", listOf("English", "Mandarin", "Communication Skills"), listOf(1, 2, 7), listOf("10:00 - 11:00", "11:00 - 12:00")),
    TutorBooking("Ayanda Ndlovu", listOf("Mathematics", "Physical Sciences", "Biology"), listOf(2, 5), listOf("14:00 - 15:00")),
    TutorBooking("Bianca Moodley", listOf("History", "Geography", "Civics"), listOf(4, 6), listOf("09:00 - 10:00")),
    TutorBooking("Christina Goncalves", listOf("Biology", "Environmental Sciences", "Ecology"), listOf(3, 7), listOf("15:00 - 16:00"))
)