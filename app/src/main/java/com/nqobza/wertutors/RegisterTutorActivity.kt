package com.nqobza.wertutors

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nqobza.wertutors.databinding.RegistertutorBinding
import com.nqobza.wetutors.LoginActivity

class RegisterTutorActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: RegistertutorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistertutorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val surname = binding.edtSurname.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            val confirmPass = binding.edtConfirmPass.text.toString()
            val number = binding.edtNum.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty() && number.isNotEmpty()) {
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


