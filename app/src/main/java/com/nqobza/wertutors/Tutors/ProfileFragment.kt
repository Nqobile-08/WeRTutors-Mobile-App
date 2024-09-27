package com.nqobza.wertutors.Tutors
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nqobza.wertutors.R

class ProfileFragment : Fragment() {

    private lateinit var edtName: EditText
    private lateinit var edtSurname: EditText
    private lateinit var edtInstitution: EditText
    private lateinit var edtNumber: EditText
    private lateinit var btnUpdate: Button
    private lateinit var txtName: TextView
    private lateinit var txtSurname: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        edtName = view.findViewById(R.id.edtName)
        edtSurname = view.findViewById(R.id.edtSurname)
        edtInstitution = view.findViewById(R.id.edtInstitution)
        edtNumber = view.findViewById(R.id.edtNumber)
        btnUpdate = view.findViewById(R.id.btnUpdate)
        txtName = view.findViewById(R.id.fullname)
        txtSurname = view.findViewById(R.id.fullnameTutor)

        loadUserData()

        btnUpdate.setOnClickListener {
            updateProfile()
        }

        return view
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        userId?.let {
            databaseReference.child(it).get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").value.toString()
                    val surname = dataSnapshot.child("surname").value.toString()
                    val institution = dataSnapshot.child("institution").value.toString()
                    val number = dataSnapshot.child("number").value.toString()

                    edtName.setText(name)
                    edtSurname.setText(surname)
                    edtInstitution.setText(institution)
                    edtNumber.setText(number)

                    txtName.text = "$name $surname"
                    txtSurname.text = surname
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProfile() {
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            val name = edtName.text.toString().trim()
            val surname = edtSurname.text.toString().trim()
            val institution = edtInstitution.text.toString().trim()
            val number = edtNumber.text.toString().trim()

            databaseReference.child(uid).child("name").setValue(name)
            databaseReference.child(uid).child("surname").setValue(surname)
            databaseReference.child(uid).child("institution").setValue(institution)
            databaseReference.child(uid).child("number").setValue(number)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        txtName.text = "$name $surname"
                        txtSurname.text = institution
                    } else {
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}