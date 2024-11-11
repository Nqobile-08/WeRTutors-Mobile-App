package com.ratjatji.eskhathinitutors.Admin

data class ChatGPTResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val message: MessageContent
    )

    data class MessageContent(
        val role: String,
        val content: String
    )

    fun getParsedResume(): String {
        return choices.firstOrNull()?.message?.content ?: "No parsed information available."
    }
}
