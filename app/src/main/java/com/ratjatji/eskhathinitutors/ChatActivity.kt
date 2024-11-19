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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<Users>
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = FirebaseAuth.getInstance()
        mDbref = FirebaseDatabase.getInstance().reference

        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter



        adapter.setOnItemClickListener { selectedUser ->
            val intent = Intent(this@ChatActivity, commsAct::class.java)
            intent.putExtra("name", selectedUser.name)
            intent.putExtra("uid", selectedUser.uid)
            startActivity(intent)
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        mDbref.child("Users").child("Tutors")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    // Log the number of tutors found
                    Log.d("ChatActivity", "Number of tutors found: ${snapshot.childrenCount}")

                    for (postSnapshot in snapshot.children) {
                        try {
                            // Validate snapshot content and parse tutor object
                            val tutor = postSnapshot.getValue(Users::class.java)
                            if (tutor != null && tutor.uid != auth.currentUser?.uid) {
                                tutor.name = "${tutor.name} ${tutor.surname}"
                                userList.add(tutor)
                                Log.d("ChatActivity", "Tutor added: ${tutor.name} ")
                            }
                        } catch (e: Exception) {
                            Log.e("ChatActivity", "Error converting tutor data: ${e.message}")
                        }
                    }

                    // Update adapter after data retrieval
                    if (userList.isEmpty()) {
                        Log.d("ChatActivity", "No tutors available to display")
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatActivity", "Database error: ${error.message}")
                }
            })





// Fetch Students
//        mDbref.reference.child("Users").child("Students")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (postSnapshot in snapshot.children) {
//                        val student = postSnapshot.getValue(Student::class.java)
//                        if (student != null && auth.currentUser?.uid != student.uid) {
//                            userList.add(student)  // Add student to userList
//                        }
//                    }
//                    adapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.e("ChatActivity", "Error fetching students: ${error.message}")
//                }
//            })
//    }
    }
}
