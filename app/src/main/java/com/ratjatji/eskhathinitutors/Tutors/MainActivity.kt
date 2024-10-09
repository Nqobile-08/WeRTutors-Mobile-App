package com.ratjatji.eskhathinitutors.Tutors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.ratjatji.eskhathinitutors.CreateReviewFragment
import com.ratjatji.eskhathinitutors.DisplayMarksFragment
import com.ratjatji.eskhathinitutors.ProfileFragment
import com.ratjatji.eskhathinitutors.R

import com.ratjatji.eskhathinitutors.SettingsHomeFragment
import com.ratjatji.eskhathinitutors.WeRTutorsAi
import com.ratjatji.eskhathinitutors.showTutorFragment
import com.ratjatji.eskhathinitutors.tutorOptions_1
import com.ratjatji.eskhathinitutors.LoadMarksFragment
import com.ratjatji.eskhathinitutors.ChatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.nav_host_fragment, SchedulesFragment())
            }
            navView.setCheckedItem(R.id.nav_dashboard)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {

                R.id.nav_dashboard -> selectedFragment = DashboardFragment()
                R.id.nav_tutor_profile -> selectedFragment = tutorOptions_1()
                R.id.nav_schedule -> selectedFragment = SchedulesFragment()
                R.id.nav_create_review -> selectedFragment = CreateReviewFragment()
                R.id.nav_session_timer -> selectedFragment = SessionTimer()
                R.id.nav_setting -> selectedFragment = SettingsHomeFragment()
                R.id.nav_logout -> selectedFragment = LogoutFragment()
                R.id.nav_AiHelper -> selectedFragment = WeRTutorsAi()
                R.id.nav_marks -> selectedFragment = LoadMarksFragment()
                R.id.nav_displayedMarks -> selectedFragment = DisplayMarksFragment()
                R.id.nav_communication -> {

                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(navView)
                    return@setNavigationItemSelectedListener true
                }

            }

            selectedFragment?.let {
                supportFragmentManager.commit {
                    replace(R.id.nav_host_fragment, it)
                }
                drawerLayout.closeDrawer(navView)
                true
            } ?: false
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {

                R.id.nav_dashboard -> selectedFragment = DashboardFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
                R.id.nav_students -> selectedFragment = SchedulesFragment()
                //  R.id.nav_setting -> selectedFragment = Setting()
            }

            selectedFragment?.let {
                supportFragmentManager.commit {
                    replace(R.id.nav_host_fragment, it)
                }
                true
            } ?: false
        }
    }
}
