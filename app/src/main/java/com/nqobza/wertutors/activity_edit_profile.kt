package com.nqobza.wertutors

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class EditProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var profileName: EditText
    private lateinit var profileEmail: EditText
    private lateinit var changePictureButton: Button
    private lateinit var saveProfileButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Initialize Views
        profilePicture = findViewById(R.id.edit_profile_picture)
        profileName = findViewById(R.id.edit_profile_name)
        profileEmail = findViewById(R.id.edit_profile_email)
        changePictureButton = findViewById(R.id.change_picture_button)
        saveProfileButton = findViewById(R.id.save_profile_button)

        // Load current user data
        loadUserData()

        // Change picture button listener
        changePictureButton.setOnClickListener {
            openImagePicker()
        }

        // Save button listener
        saveProfileButton.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun loadUserData() {
        val user: FirebaseUser? = auth.currentUser
        user?.let {
            profileName.setText(it.displayName)
            profileEmail.setText(it.email)
            // Load the profile picture using a library like Glide or Picasso (not shown here)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            profilePicture.setImageURI(imageUri)
        }
    }

    private fun saveProfileChanges() {
        val user = auth.currentUser
        val userRef = firestore.collection("users").document(user?.uid ?: "")

        val name = profileName.text.toString()
        val email = profileEmail.text.toString()

        val userUpdates = hashMapOf(
            "name" to name,
            "email" to email
        )

        userRef.update(userUpdates as Map<String, Any>).addOnCompleteListener {
            if (it.isSuccessful) {
                // Handle success (e.g., show a message)
            } else {
                // Handle failure
            }
        }

        if (imageUri != null) {
            val storageRef = storage.reference.child("profile_pictures/${user?.uid}")
            storageRef.putFile(imageUri!!).addOnSuccessListener {
                // Handle successful upload
            }.addOnFailureListener {
                // Handle failed upload
            }
        }
    }
}
