package com.ratjatji.eskhathinitutors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
class PaymentFragment : Fragment() {

    private lateinit var btnAddCard: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        // Initialize views
        btnAddCard = view.findViewById(R.id.btnAddCard)
        recyclerView = view.findViewById(R.id.rvCards)

        // Set up RecyclerView with a LinearLayoutManager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Firebase Auth and Database reference
        auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid

        // Check if user is logged in
        if (currentUserId != null) {
            dbRef = FirebaseDatabase.getInstance().getReference("Cards").child(currentUserId)
            // Fetch and display saved cards for the logged-in user
            fetchCardsFromFirebase()
        } else {
            // Notify user to log in
            Toast.makeText(requireContext(), "Please log in to view your cards.", Toast.LENGTH_SHORT).show()
        }

        // Set click listener for add card button
        btnAddCard.setOnClickListener {
            // Navigate to PaymentAddCardFragment
            val fragment = PaymentAddCardFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun fetchCardsFromFirebase() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cardList = ArrayList<Card>()
                for (snapshot in dataSnapshot.children) {
                    val card = snapshot.getValue(Card::class.java)
                    card?.let { cardList.add(it) }
                }
                if (cardList.isNotEmpty()) {
                    // Initialize the adapter with the fetched cards
                    cardAdapter = CardAdapter(cardList)
                    recyclerView.adapter = cardAdapter
                } else {
                    // No cards found, notify user
                    Toast.makeText(requireContext(), "No saved cards found.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors (e.g., log errors)
                Toast.makeText(requireContext(), "Failed to load cards: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

