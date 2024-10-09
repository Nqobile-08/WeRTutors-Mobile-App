package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.FirebaseDatabase


class RegisterTutorActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: RegisterTutorActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = RegisterTutorActivity.
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Set click listener for the Register button
        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val surname = binding.edtSurname.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            val confirmPass = binding.edtConfirmPass.text.toString()
            val phoneNum = binding.edtNum.text.toString()

            signUp(name, surname, email, password, confirmPass, phoneNum)
        }
    }

    // Sign up method for registering the tutor
    private fun signUp(
        name: String,
        surname: String,
        email: String,
        password: String,
        confirmPass: String,
        phoneNumber: String
    ) {
        if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty() && phoneNumber.isNotEmpty()) {
            if (password == confirmPass) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val database = FirebaseDatabase.getInstance().getReference("Users")
                        val tutor = Tutor(name, surname, email, uid!!,phoneNumber)

                        uid?.let { userId ->
                            database.child("Tutors").child(userId).setValue(tutor)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to save user", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
