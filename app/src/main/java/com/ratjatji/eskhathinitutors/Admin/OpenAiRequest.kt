package com.ratjatji.eskhathinitutors.Admin
data class OpenAiRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class OpenAiResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
