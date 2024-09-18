package com.nqobza.wetutors

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nqobza.wertutors.RegisterTutorActivity
import com.nqobza.wertutors.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegisterTutor.setOnClickListener{
            val intent =Intent(this, RegisterTutorActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Login unsuccessful. Try again!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}