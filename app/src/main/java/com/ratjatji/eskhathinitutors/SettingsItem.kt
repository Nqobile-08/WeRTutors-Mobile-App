package com.ratjatji.eskhathinitutors

import androidx.fragment.app.Fragment

data class SettingsItem(
    val icon: Int,
    val title: String,
    val description: String,
    val fragmentClass: Class<out Fragment>?
)