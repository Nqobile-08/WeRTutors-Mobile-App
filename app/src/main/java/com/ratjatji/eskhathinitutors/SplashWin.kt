package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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

    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.dashboard,
                    title = "Display main dashboard",
                    description = "Access all essential features from one central location. Navigate easily between tutoring sessions, AI chat, reviews, and settings. Your personalized dashboard keeps everything you need at your fingertips."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.viewtutor,
                    title = "View Tutor",
                    description = "Browse through our qualified tutors with detailed profiles showing their expertise, experience, and student ratings. Filter by subject, availability, and price range to find your perfect match for academic success."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.ai,
                    title = "AI Chat",
                    description = "Get instant homework help and study guidance from our advanced AI assistant. Available 24/7, it can explain complex topics, help solve problems, and provide study resources across all subjects. A perfect complement to your tutoring sessions."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.createreview,
                    title = "Creating a tutor review",
                    description = "Share your learning experience by rating and reviewing your tutors. Provide detailed feedback on teaching style, knowledge, and effectiveness. Your honest reviews help other students make informed choices and help tutors improve their services."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.sessiontimer,
                    title = "Session Timer",
                    description = "Track your tutoring sessions with precision using our built-in timer. Monitor session duration, set reminders for breaks, and maintain accurate records for billing and progress tracking. Helps you make the most of every minute with your tutor."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.settiings,
                    title = "Settings",
                    description = "Customize your learning experience with personalized preferences. Adjust notification settings, manage your profile, set availability preferences, and configure payment methods. Take control of your tutoring experience with easy-to-use settings."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.communication,
                    title = "Communication",
                    description = "Stay connected with your tutors and fellow students through our integrated messaging system. Schedule sessions, share study materials, ask quick questions, and receive important updates. Featuring real-time chat, file sharing, and session scheduling tools."
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