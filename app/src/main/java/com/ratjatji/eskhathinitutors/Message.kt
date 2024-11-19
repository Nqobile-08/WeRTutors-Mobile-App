package com.ratjatji.eskhathinitutors

data class Message(
    val message: String? = null,
    val senderId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "text", // "text", "image", or "file"
    var messageId: String? = null,
    val isRead: Boolean = false
) {
    constructor() : this(null, null, System.currentTimeMillis(), "text", null, false)
}
