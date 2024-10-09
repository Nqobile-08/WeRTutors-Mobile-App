package com.ratjatji.eskhathinitutors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class PaymentAddCardFragment : Fragment() {

    private lateinit var etCardNumber: EditText
    private lateinit var etExpiryDate: EditText
    private lateinit var etCvv: EditText
    private lateinit var etCardNickname: EditText
    private lateinit var btnSaveCard: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment_add_card, container, false)

        // Initialize views
        etCardNumber = view.findViewById(R.id.etCardNumber)
        etExpiryDate = view.findViewById(R.id.etExpiryDate)
        etCvv = view.findViewById(R.id.etCvv)
        etCardNickname = view.findViewById(R.id.etCardNickname)
        btnSaveCard = view.findViewById(R.id.btnSaveCard)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference.child("Cards")

        // Set click listener for save button
        btnSaveCard.setOnClickListener {
            saveCardDetails()
        }

        return view
    }

    private fun saveCardDetails() {
        val cardNumber = etCardNumber.text.toString().trim()
        val expiryDate = etExpiryDate.text.toString().trim()
        val cvv = etCvv.text.toString().trim() // **Note:** CVV is not stored
        val cardNickname = etCardNickname.text.toString().trim()

        // Input validation
        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate card number
        if (!isValidCardNumber(cardNumber)) {
            Toast.makeText(requireContext(), "Invalid card number", Toast.LENGTH_SHORT).show()
            return
        }

        // Extract first digits for IIN check
        val firstDigits = cardNumber.substring(0, Math.min(cardNumber.length, 6))

        // Check if card is Visa or Mastercard based on IIN
        val cardType = if (isVisa(firstDigits)) "Visa" else if (isMastercard(firstDigits)) "Mastercard" else "Unknown"

        // Generate a unique card ID
        val cardId = UUID.randomUUID().toString()

        // Get the current user's ID
        val userId = auth.currentUser?.uid ?: run {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Create card data (without CVV)
        val cardData = mapOf(
            "cardNumber" to cardNumber,
            "expiryDate" to expiryDate,
            "cardNickname" to cardNickname,
            "cardType" to cardType
        )

        // Save card data to Firebase, under Cards > userId > cardId
        dbRef.child(userId).child(cardId).setValue(cardData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Card saved successfully", Toast.LENGTH_LONG).show()
                    clearInputFields()
                } else {
                    Toast.makeText(requireContext(), "Error saving card: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        var sum = 0
        var isSecondDigitEven = false
        for (i in cardNumber.length - 1 downTo 0) {
            var digit = Character.getNumericValue(cardNumber[i])
            if (isSecondDigitEven) {
                digit *= 2
            }
            sum += digit / 10 + digit % 10
            isSecondDigitEven = !isSecondDigitEven
        }
        return sum % 10 == 0
    }

    private fun isVisa(firstDigits: String): Boolean {
        return firstDigits.startsWith("4")
    }

    private fun isMastercard(firstDigits: String): Boolean {
        val firstTwoDigits = firstDigits.substring(0, 2).toInt()
        return firstTwoDigits in 51..55 || firstTwoDigits == 222 || firstTwoDigits == 270 || firstTwoDigits == 271
    }

    private fun clearInputFields() {
        etCardNumber.text?.clear()
        etExpiryDate.text?.clear()
        etCvv.text?.clear()
        etCardNickname.text?.clear()
    }
}
