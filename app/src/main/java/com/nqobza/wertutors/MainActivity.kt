package com.nqobza.wetutors

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
import com.nqobza.wertutors.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(ProfileFragment())
            navView.setCheckedItem(R.id.nav_profile)
        }

        setupNavigationView()
        setupBottomNavigation()
    }

    private fun setupNavigationView() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                R.id.nav_dashboard -> replaceFragment(DashboardFragment())
                R.id.nav_students -> replaceFragment(SchedulesFragment())
                R.id.nav_library -> replaceFragment(LibraryFragment())
                R.id.nav_Jobs -> replaceFragment(JobsFragment())
                R.id.nav_logout -> replaceFragment(LogoutFragment())
                R.id.nav_communication -> {

                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(navView)
                    return@setNavigationItemSelectedListener true
                }
            }
            drawerLayout.closeDrawer(navView)
            true
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                R.id.nav_dashboard -> replaceFragment(DashboardFragment())
                R.id.nav_students -> replaceFragment(SchedulesFragment())
                else -> return@setOnNavigationItemSelectedListener false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.nav_host_fragment, fragment)
        }
    }
}