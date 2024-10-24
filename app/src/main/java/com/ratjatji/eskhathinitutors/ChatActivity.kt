package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ratjatji.eskhathinitutors.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<Users>
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbref: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = FirebaseAuth.getInstance()
        mDbref = FirebaseDatabase.getInstance()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

// In ChatActivity where you set up the RecyclerView and Adapter
        adapter.setOnItemClickListener { selectedUser ->
            val intent = Intent(this@ChatActivity, commsAct::class.java)

            // Pass user details to commsAct
            intent.putExtra("name", selectedUser.name)
            intent.putExtra("uid", selectedUser.uid)

            // Log for debugging
            Log.d("ChatActivity", "Sending name: ${selectedUser.name}, uid: ${selectedUser.uid}")

            // Start the commsAct
            startActivity(intent)
        }


        fetchUsers()
    }

    private fun fetchUsers() {
        // Clear the list to avoid duplicating entries
        userList.clear()

        // Fetch Tutors
        mDbref.reference.child("Users").child("Tutors")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val tutor = postSnapshot.getValue(Tutor::class.java)
                        if (tutor != null && auth.currentUser?.uid != tutor.uid) {
                            userList.add(tutor)  // Add tutor to userList
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatActivity", "Error fetching tutors: ${error.message}")
                }
            })

        // Fetch Students
        mDbref.reference.child("Users").child("Students")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val student = postSnapshot.getValue(Student::class.java)
                        if (student != null && auth.currentUser?.uid != student.uid) {
                            userList.add(student)  // Add student to userList
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatActivity", "Error fetching students: ${error.message}")
                }
            })
    }
}