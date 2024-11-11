package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

class WeRTutorsAi : Fragment() {

    private lateinit var etUserInput: EditText
    private lateinit var btnSend: Button
    private lateinit var tvConversation: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: ScrollView

    private lateinit var generativeModel: GenerativeModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_we_r_tutors_ai, container, false)

        etUserInput = view.findViewById(R.id.inputEditText)
        btnSend = view.findViewById(R.id.sendButton)
        tvConversation = view.findViewById(R.id.conversationTextView)
        progressBar = view.findViewById(R.id.progressBar)
        scrollView = view.findViewById(R.id.scrollView)

        // Set initial message for AI introduction
        val initialMessage = "Welcome to the Eskhathini AI Tutoring App! 🎓\n" +
                "I am your virtual tutor, here to assist you with all your educational needs. " +
                "Whether you need help with specific subjects, studying strategies, or understanding difficult concepts, " +
                "I'm here to provide tutoring support and academic guidance. " +
                "Ask me anything related to tutoring, and let's work together to improve your learning experience! 📚\n\n"
        tvConversation.text = initialMessage

        // Initialize Gemini API
        val apiKey = getString(R.string.api_key) // Replace with your actual API key
        generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
                topK = 20
                topP = 0.8f
                maxOutputTokens = 1000
            }
        )

        // Set click listener for send button
        btnSend.setOnClickListener {
            val userInput = etUserInput.text.toString()
            if (userInput.isNotEmpty()) {
                sendMessageToAI(userInput)
                etUserInput.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendMessageToAI(message: String) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = generativeModel.generateContent(message)
                val aiResponse = response.text ?: "Sorry, I couldn't generate a response."
                updateConversation("You: $message\n\nAI: $aiResponse\n\n")
            } catch (e: Exception) {
                updateConversation("Error: ${e.message}\n\n")
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateConversation(message: String) {
        tvConversation.append(message)
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }
}
