package com.nqobza.wetutors

/*class Message {
    var message: String? = null
    var senderId: String? = null

    constructor(){}

    constructor(message: String?, senderId: String?){
        this.message = message
        this.senderId = senderId
    }
}*/


data class Message(
    var message: String? = null,
    var senderId: String? = null
) {
    // No-argument constructor required by Firebase
    constructor() : this(null, null)
}