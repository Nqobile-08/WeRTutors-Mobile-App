package com.nqobza.wertutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.Fragment

class EditProfileFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSchool: EditText
    private lateinit var btnSave: Button

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        etName = view.findViewById(R.id.etFirstName)
        etSurname = view.findViewById(R.id.etSurname)
        etEmail = view.findViewById(R.id.etEmail)
        etSchool = view.findViewById(R.id.etSchool)
        btnSave = view.findViewById(R.id.btnSave)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Users/Students")

        // Load current user information
        user?.let {
            loadUserData(it.uid)
        }

        // Set onClick listener for the save button
        btnSave.setOnClickListener {
            updateUserData(user?.uid)
        }

        return view
    }

    private fun loadUserData(uid: String) {
        database.child(uid).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val name = snapshot.child("name").value.toString()
                val surname = snapshot.child("surname").value.toString()
                val email = snapshot.child("email").value.toString()
                val school = snapshot.child("school").value.toString()

                etName.setText(name)
                etSurname.setText(surname)
                etEmail.setText(email)
                etSchool.setText(school)
            }
        }.addOnFailureListener {
            // Handle the error
        }
    }

    private fun updateUserData(uid: String?) {
        if (uid != null) {
            val updatedUserData = mapOf(
                "name" to etName.text.toString(),
                "surname" to etSurname.text.toString(),
                "email" to etEmail.text.toString(),
                "school" to etSchool.text.toString()
            )

            database.child(uid).updateChildren(updatedUserData).addOnSuccessListener {
                // Handle success (e.g., show a message)
            }.addOnFailureListener {
                // Handle error
            }
        }
    }
}
