package com.ratjatji.eskhathinitutors

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

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

    // Activity result launcher for picking images
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

        // Initialize Firebase instances
        mDbRef = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        // Get intent extras
        receiverName = intent.getStringExtra("name")
        receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        // Set up room IDs
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        // Initialize views
        initializeViews()
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupMessageListener()
    }

    private fun setupMessageListener() {
        senderRoom?.let { room ->
            mDbRef.child("chats").child(room).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (messageSnapshot in snapshot.children) {
                            val message = messageSnapshot.getValue(Message::class.java)
                            message?.let { messageList.add(it) }
                        }
                        messageAdapter.notifyDataSetChanged()
                        if (messageList.isNotEmpty()) {
                            chatRecyclerView.smoothScrollToPosition(messageList.size - 1)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Failed to read messages", error.toException())
                    }
                })
        }
    }

    private fun initializeViews() {
        chatRecyclerView = findViewById(R.id.chatRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        attachmentButton = findViewById(R.id.attachmentButton)
        userName = findViewById(R.id.userName)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        // Set receiver's name in the toolbar
        userName.text = receiverName

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.hide()
    }

    private fun setupRecyclerView() {
        chatRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        chatRecyclerView.adapter = messageAdapter
    }

    private fun setupClickListeners() {
        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendTextMessage(messageText)
            }
        }

        attachmentButton.setOnClickListener {
            showAttachmentOptions()
        }
    }

    private fun showAttachmentOptions() {
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
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        pickImage.launch(intent)
    }

    private fun uploadImage(uri: Uri) {
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
            }
    }

    private fun sendTextMessage(text: String) {
        val message = Message(
            text,
            FirebaseAuth.getInstance().currentUser?.uid,
            System.currentTimeMillis(),
            type = "text"
        )
        sendMessage(message)
    }

    private fun sendMediaMessage(url: String, type: String) {
        val message = Message(
            url,
            FirebaseAuth.getInstance().currentUser?.uid,
            System.currentTimeMillis(),
            type = type
        )
        sendMessage(message)
    }

    private fun sendMessage(message: Message) {
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
                                sendNotification(message.message ?: "")
                            }
                    }
            }
        }
    }

    private fun sendNotification(messageText: String) {
        // We'll implement this in the next part
    }
}  