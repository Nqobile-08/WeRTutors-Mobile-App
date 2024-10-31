package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.api.Context
import com.ratjatji.eskhathinitutors.Tutors.MainActivity

class SplashWin : AppCompatActivity() {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var indicatorsContainer : LinearLayout

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activit_start_screen)
        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setOnboardingItems()
    {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.dashboard,
                    title = "Display main dashboard",
                    description = "Allows user direct access to main menu"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.viewtutor,
                    title = "View Tutor",
                    description = "Displays all the tutors available"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.ai,
                    title = "Ai chat",
                    description = "Ai bot which users can use to ask for assistance"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.createreview,
                    title = "Creating a tutor review",
                    description = "Allows users to rate tutor performance after tutoring session"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.sessiontimer,
                    title = "Session Timer",
                    description = "Allows user to record the length of tutor sessions"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.settiings,
                    title = "Settings",
                    description = "Application Settings"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.communication,
                    title = "Communication",
                    description = "Users are able to communication with one another"
                )
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPaper)
        onboardingViewPager.adapter = onboardingItemsAdapter
        onboardingViewPager.registerOnPageChangeCallback(object:
        ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        findViewById<ImageView>(R.id.imageNext).setOnClickListener{
            if(onboardingViewPager.currentItem+1 < onboardingItemsAdapter.itemCount)
            {
                onboardingViewPager.currentItem += 1
            }
            else
            {
                navigateToDashBoardActivity()
            }
        }
        findViewById<TextView>(R.id.textSkip).setOnClickListener{
            navigateToDashBoardActivity()
        }
        findViewById<TextView>(R.id.buttonGetStarted).setOnClickListener{
            navigateToDashBoardActivity()
        }
    }

    private fun navigateToDashBoardActivity()
    {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }


private fun setupIndicators()
    {
    indicatorsContainer =findViewById(R.id.indicatorContainer)
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices)
        {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let{
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position:Int)
    {
        val childCount = indicatorsContainer.childCount
        for(i in 0 until childCount)
        {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if(i == position)
            {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }

}