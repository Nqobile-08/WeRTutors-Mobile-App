package com.ratjatji.eskhathinitutors.Admin
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.ratjatji.eskhathinitutors.ProfileFragment
import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.Tutors.QuickNavFragment

class AdminMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.nav_host_fragment, ProfileFragment())
            }
            navView.setCheckedItem(R.id.nav_dashboard)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {

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

                R.id.nav_dashboard -> selectedFragment = QuickNavFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
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