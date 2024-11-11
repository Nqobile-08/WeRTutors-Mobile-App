package com.ratjatji.eskhathinitutors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class UserAdapter(val context: Context, val userList: ArrayList<Users>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // Step 1: Define an interface for item clicks
    private var onItemClickListener: ((Users) -> Unit)? = null

    // Step 2: Allow ChatActivity to set the click listener
    fun setOnItemClickListener(listener: (Users) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.name

        // Step 3: Trigger the click listener when the user clicks on an item
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentUser) // Pass the current user to the listener
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.txt_name)
    }
}
