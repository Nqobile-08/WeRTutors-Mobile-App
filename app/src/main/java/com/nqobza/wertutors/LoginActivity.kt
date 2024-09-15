package com.nqobza.wertutors

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nqobza.wertutors.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupLoginButton()
        setupRegisterButton()
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener { loginUser() }
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener { navigateToRegisterActivity() }
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    FirebaseDatabase.getInstance().reference.child("Users").child(userId!!)
                        .get().addOnSuccessListener { snapshot ->
                            val role = snapshot.child("role").value.toString()
                            if(role == "student")
                            {
                                Toast.makeText(this, "Login successful, Student", Toast.LENGTH_SHORT).show()
                            }
                            else if (role == "admin")
                            {
                                Toast.makeText(this, "Welcome ADMIN",Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed Login Attempt", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}