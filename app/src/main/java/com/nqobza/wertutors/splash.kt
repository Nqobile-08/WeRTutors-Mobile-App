package com.nqobza.wetutors



import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nqobza.wertutors.R
import com.nqobza.wertutors.Tutors.LoginActivity


class splash : AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        handler= Handler()
        handler.postDelayed({
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}