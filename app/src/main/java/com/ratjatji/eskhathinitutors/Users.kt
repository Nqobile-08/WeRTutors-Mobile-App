package com.ratjatji.eskhathinitutors

// Parent Users class
open class Users {
    var name: String = ""
    var surname: String = ""
    var email: String = ""
    var uid: String = ""

    // No-argument constructor required by Firebase
    constructor()

    // Secondary constructor
    constructor(name: String, surname: String, email: String, uid: String) {
        this.name = name
        this.surname = surname
        this.email = email
        this.uid = uid
    }
}

// Tutor class
class Tutor : Users {


    var phoneNumber: String = ""

    // No-argument constructor required by Firebase
    constructor() : super()

    // Secondary constructor
    constructor(name: String, surname: String, email: String, uid: String, phoneNumber: String) : super(name, surname, email, uid) {
        this.phoneNumber = phoneNumber
    }
}

// Student class
class Student : Users {
    var school: String = ""

    // No-argument constructor required by Firebase
    constructor() : super()

    // Secondary constructor
    constructor(name: String, surname: String, email: String, uid: String, school: String) : super(name, surname, email, uid) {
        this.school = school
    }
}