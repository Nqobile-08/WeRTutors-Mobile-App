package com.nqobza.wertutors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "TestFragment"

private lateinit var dbRef: DatabaseReference
private lateinit var reviewRecyclerView: RecyclerView
private lateinit var reviewAdapter: ReviewAdapter
private lateinit var reviewArrayList: ArrayList<Review>



class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_test, container, false)

        // Log the fragment creation
        Log.d(TAG, "Fragment created")

        // Initialize RecyclerView here after inflating the layout
        reviewRecyclerView = view.findViewById(R.id.rvReviews)
        reviewRecyclerView.layoutManager = LinearLayoutManager(context)

       //undo reviewArrayList = arrayListOf()
// Call method to fetch review data
        //getReviewData()

        loadReviewsFromFirebase()

        return view
    }

    private fun getReviewData() {
        // Log Firebase database reference initialization
        Log.d(TAG, "Fetching data from Firebase")

        dbRef = FirebaseDatabase.getInstance().getReference("student_reviews")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "Data received: ${snapshot.exists()}")

                if (snapshot.exists()) {
                    reviewArrayList.clear()  // Clear the list before adding new data
                    for (reviewSnapshot in snapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        if (review != null) {
                            reviewArrayList.add(review)
                            Log.d(TAG, "Review added: ${review.tutorName}")
                        } else {
                            Log.e(TAG, "Failed to parse review data")
                        }
                    }
                    reviewRecyclerView.adapter = ReviewAdapter(reviewArrayList)
                    Log.d(TAG, "Adapter set with ${reviewArrayList.size} reviews")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error: ${error.message}")
            }
        })
    }
    private fun loadReviewsFromFirebase() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("student_reviews")

        // Fetch reviews for a specific tutor
        databaseRef.child("Lerato Mokwoena")  // Replace with the actual tutor name key or path
            .get().addOnSuccessListener { snapshot ->
                val reviews = mutableListOf<Review>()
                for (reviewSnapshot in snapshot.children) {
                    val review = reviewSnapshot.getValue(Review::class.java)
                    if (review != null) {
                        reviews.add(review)
                    }
                }

                // Pass reviews to the adapter
                reviewAdapter = ReviewAdapter(reviews)
                reviewRecyclerView.adapter = reviewAdapter
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to load reviews", Toast.LENGTH_SHORT).show()
            }
    }
}
