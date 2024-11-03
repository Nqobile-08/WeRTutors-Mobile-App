package com.ratjatji.eskhathinitutors

data class Card(
    var cardID: String? = null,
    var cardType: String? = null,
    var cardNumber: String? = null,
    var cardNickname: String? = null,
    var expiryDate: String? = null,
) {
    constructor() : this(null, null, null, null, null)
}
