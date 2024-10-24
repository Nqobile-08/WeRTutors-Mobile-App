package com.ratjatji.eskhathinitutors


data class Message(
    var message: String? = null,
    var senderId: String? = null
) {
    // No-argument constructor required by Firebase
    constructor() : this(null, null)
}