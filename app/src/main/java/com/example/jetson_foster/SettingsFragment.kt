package com.example.jetson_foster

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.jetson_foster.profile.ProfileActivity

class SettingsFragment : PreferenceFragmentCompat() {

    //lateinit var profileEditButton: EditTextPreference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

       val profileEditButton = findPreference<Preference>(getString(R.string.settings_edit_prof_button))
        profileEditButton?.setOnPreferenceClickListener {
            Log.d("jetbug","BUTTON CLICKED\n")
            val intent = Intent(activity, ProfileActivity::class.java)
            activity?.startActivity(intent)
            true
        }
        val webpageButton = findPreference<Preference>(getString( R.string.settings_webpage_button))
        webpageButton?.setOnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sfu.ca/computing.html"))
            activity?.startActivity(browserIntent)
            true
        }

        val sPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        println("${sPref}")
    }


}