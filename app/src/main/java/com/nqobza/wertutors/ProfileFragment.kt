package com.nqobza.wertutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class ProfileFragment : Fragment() {

    private lateinit var profilePicture: ImageView
    private lateinit var fullName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var editProfileButton: Button
private lateinit var profileName: TextView
private lateinit var fullNameTutor: TextView
private  lateinit var billing : ImageView
private lateinit var calender: ImageView
private lateinit var billingtext: TextView
private lateinit var booking: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)


    }
    }




