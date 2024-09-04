package com.nqobza.wetutors

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

import com.google.android.material.navigation.NavigationView
import com.nqobza.wertutors.DashboardFragment
import com.nqobza.wertutors.JobsFragment
import com.nqobza.wertutors.R
import com.yourpackage.name.ProfileFragment


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
                replace(R.id.nav_host_fragment,ProfileFragment())
            }
            navView.setCheckedItem(R.id.nav_profile)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.nav_profile -> selectedFragment = ProfileFragment()
                R.id.nav_dashboard -> selectedFragment = DashboardFragment()
                R.id.nav_students -> selectedFragment = SchedulesFragment()
                R.id.nav_library -> selectedFragment = LibraryFragment()
                R.id.nav_Jobs -> selectedFragment = JobsFragment()
                R.id.nav_logout -> selectedFragment = LogoutFragment()
            }

            selectedFragment?.let {
                supportFragmentManager.commit {
                    replace(R.id.nav_host_fragment, it)
                }
                drawerLayout.closeDrawer(navView)
                true
            } ?: false
        }
    }
}
