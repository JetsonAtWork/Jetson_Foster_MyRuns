package com.example.jetson_foster.profile

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetson_foster.R
import com.google.gson.Gson


/**
 * TODO: use this to replace old clunky code in ProfileActivity
 */
class ProfileViewModel(val sPrefs: SharedPreferences,
                       val editor: SharedPreferences.Editor) : ViewModel() {
    val gson: Gson = Gson()

    private val profileData: MutableLiveData<ProfileData> by lazy {
        MutableLiveData<ProfileData>().also {
            loadProfile()
        }
    }

    fun getData(): LiveData<ProfileData> {
        return profileData
    }

    private fun loadProfile(): ProfileData {
        val json = sPrefs.getString(R.string.pref_profile_data.toString(),"")
        val data = gson.fromJson(json, ProfileData::class.java)
        if (data == null) {

        }
        return data
    }

    fun saveProfile() {

    }
}