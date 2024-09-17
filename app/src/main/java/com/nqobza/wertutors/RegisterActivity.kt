package com.nqobza.wertutors
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nqobza.wertutors.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding:  ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val surname = binding.edtRSurname.text.toString().trim()
            val email = binding.edtREmail.text.toString().trim()
            val password = binding.edtRPass.text.toString().trim()
            val school = binding.edtRSchool.text.toString().trim()

            if(validateInput(name, surname, email, password, school))
            {
                createUser(name, surname, email, password, school, isAdmin = false)
            }
        }
    }

    private fun validateInput(name:String, surname:String, email: String, password: String, school:String) :Boolean
    {
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || school.isEmpty())
        {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!isPasswordValid(password))
        {
            Toast.makeText(this, "Password must be at least 12 characters long, contain a number, special character, uppercase, and lowercase.", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun isPasswordValid  (password:String):Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{12,}$"
        return password.matches(Regex(passwordPattern))
    }

    private fun createUser(name:String, surname:String, email: String, password: String, school:String, isAdmin:Boolean) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Store user role (Student/Tutor/Admin) in database
                    val userId = auth.currentUser?.uid
                    val role = if(isAdmin) "admin" else "student"

                    val userMap = mapOf(
                        "name" to name,
                        "surname" to surname,
                        "email" to email,
                        "school" to school,
                        "role" to role
                    )

                    FirebaseDatabase.getInstance().reference.child("Users").child(userId!!)
                        .setValue(userMap).addOnCompleteListener{
                            if(it.isSuccessful){
                                Toast.makeText(this, "Registration successful!",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(this, "Failed to register user",Toast.LENGTH_SHORT).show()
                            }
                        }

                }
                else {
                    Toast.makeText(baseContext, "Registration Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}