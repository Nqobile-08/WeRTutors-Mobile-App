package com.ratjatji.eskhathinitutors.Tutors


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.ratjatji.eskhathinitutors.ATSFragment
import com.ratjatji.eskhathinitutors.ChatActivity
import com.ratjatji.eskhathinitutors.CreateReviewFragment
import com.ratjatji.eskhathinitutors.DisplayMarksFragment
import com.ratjatji.eskhathinitutors.LoadMarksFragment
import com.ratjatji.eskhathinitutors.NotificationHelper
import com.ratjatji.eskhathinitutors.ProfileFragment
import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.ScheduleCalendarFragment
import com.ratjatji.eskhathinitutors.SettingsHomeFragment
import com.ratjatji.eskhathinitutors.SplashWin
import com.ratjatji.eskhathinitutors.WeRTutorsAi
import com.ratjatji.eskhathinitutors.tutorOptions_1
import java.util.Locale


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        loadLanguagePreference()
        setContentView(R.layout.activity_main)

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
                replace(R.id.nav_host_fragment, SchedulesFragment())
            }
            navView.setCheckedItem(R.id.nav_dashboard)
        }
        NotificationHelper.createNotificationChannels(this)

        navView.setNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {

                R.id.nav_dashboard -> selectedFragment = QuickNavFragment()
                R.id.nav_tutor_profile -> selectedFragment = tutorOptions_1()
                R.id.nav_schedule -> selectedFragment = SchedulesFragment()
                R.id.nav_create_review -> selectedFragment = CreateReviewFragment()
                R.id.nav_session_timer -> selectedFragment = SessionTimer()
                R.id.nav_setting -> selectedFragment = SettingsHomeFragment()
                R.id.nav_logout -> selectedFragment = LogoutFragment()
                R.id.nav_AiHelper -> selectedFragment = WeRTutorsAi()
                R.id.nav_ATS -> selectedFragment = ATSFragment()
                R.id.nav_marks -> selectedFragment = LoadMarksFragment()
                R.id.nav_displayedMarks -> selectedFragment = DisplayMarksFragment()
               // R.id.nav_tutor -> selectedFragment = NotificationsFragment()
               // R.id.nav_change_language -> selectedFragment = ChangeLanguageFragment()
                R.id.nav_cal -> selectedFragment = ScheduleCalendarFragment()
               // R.id.nav_fcmessage -> selectedFragment = FcmMessageFragment()
                R.id.nav_nav -> {

                    val intent = Intent(this, SplashWin::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(navView)
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_communication -> {

                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(navView)
                    return@setNavigationItemSelectedListener true


                }

            }
            // Creates a button that mimics a crash when pressed


            selectedFragment?.let {
                supportFragmentManager.commit {
                    replace(R.id.nav_host_fragment, it)
                }
                drawerLayout.closeDrawer(navView)
                true
            } ?: false
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener setNavigationItemSelectedListener@{ item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {

                R.id.nav_dashboard -> selectedFragment = QuickNavFragment()
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

    private fun loadLocale() {
        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("My_Lang", "en")
        setAppLocale(this, languageCode ?: "en")
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun loadLanguagePreference() {
        val sharedPreferences: android.content.SharedPreferences? =
            getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences?.getString("LANGUAGE_CODE", "en")
        setLocale(languageCode!!)
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(

        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("firebaselogs", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.w("firebaselogs", "Token: $token")



            })
        } else {

        }

    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED

            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
