package com.ratjatji.eskhathinitutors.Tutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.card.MaterialCardView
import com.ratjatji.eskhathinitutors.*
import com.ratjatji.eskhathinitutors.databinding.TutorOptions1Binding

class QuickNavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quick_nav, container, false)

        // Bind each card to its respective fragment or activity
        view.findViewById<MaterialCardView>(R.id.profileCard).setOnClickListener {
            navigateToFragment(ProfileFragment())
        }

        view.findViewById<MaterialCardView>(R.id.settingsCard).setOnClickListener {
            navigateToFragment(SettingsHomeFragment())
        }

        view.findViewById<MaterialCardView>(R.id.createReviewCard).setOnClickListener {
            navigateToFragment(CreateReviewFragment())
        }

        view.findViewById<MaterialCardView>(R.id.aiHelperCard).setOnClickListener {
            navigateToFragment(WeRTutorsAi())
        }

        view.findViewById<MaterialCardView>(R.id.progressCard).setOnClickListener {
            navigateToFragment(DisplayMarksFragment())
        }

        view.findViewById<MaterialCardView>(R.id.sessionCard).setOnClickListener {
            navigateToFragment(ScheduleCalendarFragment())
        }

        view.findViewById<MaterialCardView>(R.id.ViewCalenderCard).setOnClickListener {
            navigateToFragment(ScheduleCalendarFragment())
        }

        view.findViewById<MaterialCardView>(R.id.ChatCard).setOnClickListener {
            startActivity(ChatActivity::class.java)
        }

        view.findViewById<MaterialCardView>(R.id.howToGuideCard).setOnClickListener {
            startActivity(SplashWin::class.java)
        }


        // Add any additional card listeners as needed

        return view
    }

    // Helper function to navigate to fragments
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            replace(R.id.nav_host_fragment, fragment)
            addToBackStack(null)
        }
    }

    // Helper function to start activities
    private fun startActivity(activityClass: Class<*>) {
        val intent = Intent(requireContext(), activityClass)
        startActivity(intent)
    }
}
