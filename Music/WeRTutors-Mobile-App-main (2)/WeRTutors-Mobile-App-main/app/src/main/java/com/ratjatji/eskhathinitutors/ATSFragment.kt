package com.ratjatji.eskhathinitutors

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.ai.client.generativeai.GenerativeModel
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ATSFragment : Fragment() {

    private lateinit var btnUploadResume: Button
    private lateinit var tvExtractedData: TextView
    private lateinit var btnProcessResume: Button
    private val PICK_FILE_REQUEST_CODE = 1
    private var resumeFileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_a_t_s, container, false)

        // Binding UI components
        btnUploadResume = view.findViewById(R.id.btnUploadResume)
        tvExtractedData = view.findViewById(R.id.tvExtractedData)
        btnProcessResume = view.findViewById(R.id.btnProcessResume)

        // Upload Resume Button Click Listener
        btnUploadResume.setOnClickListener {
            openFilePicker()
        }

        // Process Resume Button Click Listener
        btnProcessResume.setOnClickListener {
            resumeFileUri?.let {
                processResume(it)
            }
        }

        return view
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                resumeFileUri = it
                btnProcessResume.visibility = View.VISIBLE
            }
        }
    }

    private fun processResume(uri: Uri) {
        CoroutineScope(Dispatchers.Main).launch {
            val extractedText = withContext(Dispatchers.IO) {
                extractTextFromResume(uri)
            }
            tvExtractedData.text = extractedText
            tvExtractedData.visibility = View.VISIBLE

            // Call the Gemini API after extracting the text
            if (extractedText.isNotBlank()) {
                callGeminiApi(extractedText)
            } else {
                tvExtractedData.text = "Failed to extract text from the resume. Please try again with a valid PDF."
            }
        }
    }

    private fun extractTextFromResume(uri: Uri): String {
        return try {
            val inputStream: InputStream? = context?.contentResolver?.openInputStream(uri)
            if (inputStream == null) {
                return "Error extracting text from resume. Unable to open file."
            }

            val tempFile = File(context?.cacheDir, "tempResume.pdf")
            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            val pdfReader = PdfReader(tempFile)
            val pdfDocument = PdfDocument(pdfReader)
            val pdfText = StringBuilder()

            for (i in 1..pdfDocument.numberOfPages) {
                val page = pdfDocument.getPage(i)
                val text = PdfTextExtractor.getTextFromPage(page)
                pdfText.append(text).append("\n")

            }

            pdfDocument.close()
            pdfReader.close()

            pdfText.toString().takeIf { it.isNotBlank() } ?: "No text found in the resume."
        } catch (e: Exception) {
            Log.e("ATSFragment", "Error extracting text from resume: ${e.message}")
            "Error extracting text from resume. ${e.message}"
        }
    }

    private fun callGeminiApi(resumeText: String) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyD7nFgqvRPZ1vzeii9iFtY2a001jRMT23Y" // Replace with your actual API key
        )

        val prompt = "The candidate is applying to become a  tutor you need to look at their" +
                " requirements that they have. If it is above 75% or if they got 72% for" +
                " that subject return a message saying Yes and if and if it does not meet the " +
                "requirement then return No. Then return Yes +  $resumeText"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = generativeModel.generateContent(prompt)
                withContext(Dispatchers.Main) {
                    tvExtractedData.text = response.text
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ATSFragment", "Error generating content: ${e.message}")
                    tvExtractedData.text = "Error processing the resume. Please try again."
                }
            }
        }
    }
}
