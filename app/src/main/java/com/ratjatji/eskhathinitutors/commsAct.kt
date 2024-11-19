package com.ratjatji.eskhathinitutors

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class commsAct : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var attachmentButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mStorage: FirebaseStorage
    private lateinit var userName: TextView

    private var receiverRoom: String? = null
    private var senderRoom: String? = null
    private var receiverUid: String? = null
    private var receiverName: String? = null

    companion object {
        private const val TAG = "commsAct"
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                uploadImage(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comms)

        try {
            // Initialize Firebase instances
            mDbRef = FirebaseDatabase.getInstance().reference
            mStorage = FirebaseStorage.getInstance()

            // Get intent extras for full name and UID
            receiverName = intent.getStringExtra("name")
            receiverUid = intent.getStringExtra("uid")
            val senderUid = FirebaseAuth.getInstance().currentUser?.uid

            // Set up room IDs
            senderRoom = receiverUid + senderUid
            receiverRoom = senderUid + receiverUid

            // Initialize views and setup
            initializeViews()
            setupToolbar()
            setupRecyclerView()
            setupClickListeners()
            setupMessageListener()

        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            Toast.makeText(this, "Error initializing chat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeViews() {
        try {
            chatRecyclerView = findViewById(R.id.chatRecycleView)
            messageBox = findViewById(R.id.messageBox)
            sendButton = findViewById(R.id.sendButton)
            attachmentButton = findViewById(R.id.attachmentButton)
            userName = findViewById(R.id.userName)

            messageList = ArrayList()
            messageAdapter = MessageAdapter(this, messageList)

            // Display the full name in the toolbar
            userName.text = receiverName

            findViewById<ImageButton>(R.id.backButton).setOnClickListener {
                onBackPressed()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views", e)
        }
    }

    private fun setupToolbar() {
        supportActionBar?.hide()
    }

    private fun setupRecyclerView() {
        try {
            chatRecyclerView.layoutManager = LinearLayoutManager(this).apply {
                stackFromEnd = true
            }
            chatRecyclerView.adapter = messageAdapter
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up RecyclerView", e)
        }
    }

    private fun setupClickListeners() {
        try {
            sendButton.setOnClickListener {
                val messageText = messageBox.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    sendTextMessage(messageText)
                }
            }

            attachmentButton.setOnClickListener {
                showAttachmentOptions()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up click listeners", e)
        }
    }

    private fun setupMessageListener() {
        receiverRoom?.let { room ->
            mDbRef.child("chats").child(room).child("messages")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        try {
                            val message = snapshot.getValue(Message::class.java)
                            message?.let {
                                if (it.timestamp == 0L) {
                                    Log.w(TAG, "Received message with invalid timestamp")
                                    return
                                }
                                messageList.add(it)
                                messageAdapter.updateMessages()

                                // Post scroll to main thread to ensure it happens after layout
                                chatRecyclerView.post {
                                    try {
                                        val newPosition = messageAdapter.itemCount - 1
                                        if (newPosition >= 0) {
                                            chatRecyclerView.smoothScrollToPosition(newPosition)
                                        }
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error scrolling to bottom", e)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error processing new message", e)
                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Failed to read messages", error.toException())
                    }
                })
        }
    }

    private fun showAttachmentOptions() {
        try {
            val options = arrayOf("Image", "File")
            AlertDialog.Builder(this)
                .setTitle("Select Attachment Type")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> openImagePicker()
                        1 -> openFilePicker()
                    }
                }
                .show()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing attachment options", e)
        }
    }

    private fun openImagePicker() {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening image picker", e)
            Toast.makeText(this, "Unable to open image picker", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFilePicker() {
        try {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            pickImage.launch(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening file picker", e)
            Toast.makeText(this, "Unable to open file picker", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(uri: Uri) {
        try {
            val progressDialog = AlertDialog.Builder(this)
                .setView(layoutInflater.inflate(R.layout.progress_dialog, null))
                .setCancelable(false)
                .create()
                .apply {
                    show()
                }

            val ref = mStorage.reference.child("chat_images/${UUID.randomUUID()}")
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { downloadUri ->
                        progressDialog.dismiss()
                        sendMediaMessage(downloadUri.toString(), "image")
                    }
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to upload image", it)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error in uploadImage", e)
            Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendTextMessage(text: String) {
        try {
            val message = Message(
                text,
                FirebaseAuth.getInstance().currentUser?.uid,
                System.currentTimeMillis(),
                type = "text"
            )
            sendMessage(message)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending text message", e)
            Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMediaMessage(url: String, type: String) {
        try {
            val message = Message(
                url,
                FirebaseAuth.getInstance().currentUser?.uid,
                System.currentTimeMillis(),
                type = type
            )
            sendMessage(message)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending media message", e)
            Toast.makeText(this, "Error sending media", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage(message: Message) {
        try {
            senderRoom?.let { sRoom ->
                receiverRoom?.let { rRoom ->
                    val messageId = mDbRef.child("chats").child(sRoom).child("messages").push().key
                    message.messageId = messageId

                    mDbRef.child("chats").child(sRoom).child("messages").child(messageId!!)
                        .setValue(message)
                        .addOnSuccessListener {
                            mDbRef.child("chats").child(rRoom).child("messages").child(messageId)
                                .setValue(message)
                                .addOnSuccessListener {
                                    messageBox.setText("")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error sending message to receiver room", e)
                                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error sending message to sender room", e)
                            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in sendMessage", e)
            Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show()
        }
    }
}