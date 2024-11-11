package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class SettingsAdapter(
    private val settingsList: List<SettingsItem>,
    private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    inner class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.imgSettingIcon)
        val tvTitle: TextView = itemView.findViewById(R.id.tvSettingTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvSettingDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_settings_options, parent, false)
        return SettingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val settingsItem = settingsList[position]
        holder.ivIcon.setImageResource(settingsItem.icon)
        holder.tvTitle.text = settingsItem.title
        holder.tvDescription.text = settingsItem.description

        holder.itemView.setOnClickListener {
            handleSettingsItemClick(settingsItem.fragmentClass)
        }
    }

    override fun getItemCount(): Int {
        return settingsList.size
    }

    private fun handleSettingsItemClick(fragmentClass: Class<out Fragment>?) {
        when (fragmentClass) {
            ProfileFragment::class.java -> {
                loadFragment(ProfileFragment())
            }
            LanguageSelectionFragment::class.java -> {
                loadFragment(LanguageSelectionFragment())
            }
            SettingsDarkModeFragment::class.java -> {
                loadFragment(SettingsDarkModeFragment())
            }
            PaymentFragment::class.java -> {
                loadFragment(PaymentFragment())
            }
            null -> {
                // Handle logout
                val intent = Intent(fragmentActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                fragmentActivity.startActivity(intent)
                 fragmentActivity.finish()
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        fragmentActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}