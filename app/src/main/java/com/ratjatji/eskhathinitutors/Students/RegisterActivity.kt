package com.ratjatji.eskhathinitutors.Students

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth


import com.ratjatji.eskhathinitutors.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val surname = binding.edtSurname.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            val confirmPass = binding.edtConfirmPass.text.toString()
            val school = binding.edtSchool.text.toString()


            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty() && school.isNotEmpty()) {
                if (password == confirmPass) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Empty fields are not allowed, fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}