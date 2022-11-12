package com.example.jetson_foster

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainFragmentStateAdapter(activity: FragmentActivity, inputFrags: ArrayList<Fragment>): FragmentStateAdapter(activity) {

    var fragList : ArrayList<Fragment> = inputFrags;

    override fun getItemCount(): Int {
        return fragList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragList[position]
    }
}