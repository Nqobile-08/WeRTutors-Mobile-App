package com.ratjatji.eskhathinitutors

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var currentUser: FirebaseUser? = null

    private lateinit var welcomeMessageTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var userTypeTextView: TextView
    private lateinit var uidTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var residentialAddressEditText: EditText
    private lateinit var suburbsEditText: EditText
    private lateinit var tertiaryInstitutionEditText: EditText
    private lateinit var profileImage: ImageView
    private lateinit var changeProfilePhotoButton: Button
    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase and check for current user
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return view
        }

        initializeViews(view)
        setupListeners(view)
        loadUserData()

        return view
    }

    private fun initializeViews(view: View) {
        welcomeMessageTextView = view.findViewById(R.id.welcomeMessageTextView)
        profileImage = view.findViewById(R.id.profile_image)
        fullNameTextView = view.findViewById(R.id.fullname)
        userTypeTextView = view.findViewById(R.id.userType)
        uidTextView = view.findViewById(R.id.uidTextView)
        nameEditText = view.findViewById(R.id.edtName)
        surnameEditText = view.findViewById(R.id.edtSurname)
        residentialAddressEditText = view.findViewById(R.id.edtResidentialAddress)
        suburbsEditText = view.findViewById(R.id.edtSuburbs)
        tertiaryInstitutionEditText = view.findViewById(R.id.edtTertiaryInstitution)
        changeProfilePhotoButton = view.findViewById(R.id.change_profile_image_button)
    }

    private fun setupListeners(view: View) {
        changeProfilePhotoButton.setOnClickListener { pickImage() }
        view.findViewById<Button>(R.id.btnUpdate).setOnClickListener { updateUserData() }
    }

    private fun loadUserData() {
        currentUser?.let { user ->
            welcomeMessageTextView.text = "Welcome, ${user.email}"
            uidTextView.text = "UID: ${user.uid}"

            val userRef = database.getReference("users").child(user.uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(User::class.java)
                    userData?.let {
                        fullNameTextView.text = "${it.name} ${it.surname}"
                        userTypeTextView.text = if (it.userType == "tutor") "Tutor" else "Student"
                        nameEditText.setText(it.name)
                        surnameEditText.setText(it.surname)
                        residentialAddressEditText.setText(it.residentialAddress)
                        suburbsEditText.setText(it.suburbs)
                        tertiaryInstitutionEditText.setText(it.tertiaryInstitution)

                        // Load the profile image if available
                        if (!it.profileImageUrl.isNullOrEmpty()) {
                            Picasso.get()
                                .load(it.profileImageUrl)
                                .placeholder(R.drawable.default_profile_image)
                                .into(profileImage)
                        } else {
                            profileImage.setImageResource(R.drawable.default_profile_image)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to load user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            profileImage.setImageURI(imageUri)
            uploadImage()
        }
    }

    private fun uploadImage() {
        imageUri?.let { uri ->
            currentUser?.let { user ->
                val ref = storage.reference.child("profile_images/${user.uid}")
                ref.putFile(uri).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                        updateUserProfileImage(downloadUrl.toString())
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to upload image: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUserProfileImage(profileImageUrl: String) {
        currentUser?.let { user ->
            val userRef = database.getReference("users").child(user.uid)
            userRef.child("profileImageUrl").setValue(profileImageUrl)
        }
    }

    private fun updateUserData() {
        currentUser?.let { user ->
            val userRef = database.getReference("users").child(user.uid)
            val updates = hashMapOf<String, Any>(
                "name" to nameEditText.text.toString(),
                "surname" to surnameEditText.text.toString(),
                "residentialAddress" to residentialAddressEditText.text.toString(),
                "suburbs" to suburbsEditText.text.toString(),
                "tertiaryInstitution" to tertiaryInstitutionEditText.text.toString()
            )

            userRef.updateChildren(updates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    loadUserData() // Reload user data to reflect changes
                } else {
                    Toast.makeText(context, "Failed to update profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    data class User(
        val uid: String = "",
        val name: String = "",
        val surname: String = "",
        val userType: String = "",
        val residentialAddress: String = "",
        val suburbs: String = "",
        val tertiaryInstitution: String = "",
        val profileImageUrl: String = ""
    )
}
