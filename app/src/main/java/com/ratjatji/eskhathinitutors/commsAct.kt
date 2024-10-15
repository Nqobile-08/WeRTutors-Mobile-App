package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class commsAct : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbref: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comms)

        // Getting the name and UID of the receiver
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid


        mDbref = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        // Set the title of the chat (receiver's name)
        supportActionBar?.title = name

        // Initialize the UI components
        chatRecyclerView = findViewById(R.id.chatRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        // Setup RecyclerView with the adapter and layout manager
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        // Fetch and display messages from Firebase
        mDbref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear() // Clear the list to avoid duplicates
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        if (message != null) {
                            Log.d("commsAct", "Message fetched: ${message.message}")
                            messageList.add(message)
                        }
                    }

                    messageAdapter.notifyDataSetChanged()

                    // Check if there are any messages to scroll to
                    if (messageList.isNotEmpty()) {
                        chatRecyclerView.scrollToPosition(messageList.size - 1)
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Log.e("commsAct", "Database error: ${error.message}")
                }
            })

        // Send message when the send button is clicked
        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()

            if (messageText.isNotEmpty()) {
                val messageObject = Message(messageText, senderUid)

                // Send message to both sender's and receiver's rooms
                mDbref.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbref.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }

                // Clear the message input box after sending
                messageBox.setText("")
            }
        }
    }
}
