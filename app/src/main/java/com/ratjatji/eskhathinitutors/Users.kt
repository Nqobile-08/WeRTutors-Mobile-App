package com.ratjatji.eskhathinitutors

// Base Users class
open class Users {
    var name: String = ""
    var surname: String = ""
    var email: String = ""
    var uid: String = ""
    var userType: String = "" // To distinguish between Tutor and Student

    // No-argument constructor required by Firebase
    constructor()

    // Secondary constructor
    constructor(name: String, surname: String, email: String, uid: String, userType: String) {
        this.name = name
        this.surname = surname
        this.email = email
        this.uid = uid
        this.userType = userType
    }

    override fun toString(): String {
        return "Users(name='$name', surname='$surname', email='$email', uid='$uid', userType='$userType')"
    }
}

// Tutor class without fcmToken and isAvailable
class Tutor : Users {
    var phoneNumber: String = ""

    // No-argument constructor required by Firebase
    constructor() : super() {
        userType = "tutor"
    }

    // Secondary constructor
    constructor(
        name: String,
        surname: String,
        email: String,
        uid: String,
        phoneNumber: String
    ) : super(name, surname, email, uid, "tutor") {
        this.phoneNumber = phoneNumber
    }

    override fun toString(): String {
        return "Tutor(${super.toString()}, phoneNumber='$phoneNumber')"
    }
}

// Student class remains the same
class Student : Users {
    var school: String = ""
    var grade: String = "" // Optional: add grade level

    // No-argument constructor required by Firebase
    constructor() : super() {
        userType = "student"
    }

    // Secondary constructor
    constructor(
        name: String,
        surname: String,
        email: String,
        uid: String,
        school: String,
        grade: String = ""
    ) : super(name, surname, email, uid, "student") {
        this.school = school
        this.grade = grade
    }

    override fun toString(): String {
        return "Student(${super.toString()}, school='$school', grade='$grade')"
    }
}
