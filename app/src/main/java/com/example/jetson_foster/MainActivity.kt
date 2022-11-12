package com.example.jetson_foster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.jetson_foster.history.HistoryFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var fragStateAdapter: MainFragmentStateAdapter
    private lateinit var viewPager : ViewPager2
    private lateinit var tabs : TabLayout
    private lateinit var startFrag : StartFragment
    private lateinit var historyFrag : HistoryFragment
    private lateinit var settingsFrag : SettingsFragment
    private lateinit var fragList : ArrayList<Fragment>
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private val tabTitles = arrayOf("Start","History","Settings")
    private val VIEWPAGER_SENSITIVITY = 6


    override fun onCreate(savedInstanceState: Bundle?) {
        startFrag = StartFragment()
        historyFrag = HistoryFragment()
        settingsFrag = SettingsFragment()
        fragList = arrayListOf(startFrag, historyFrag, settingsFrag)

        fragStateAdapter = MainFragmentStateAdapter(this,fragList)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewpager)
        viewPager.reduceDragSensitivity()
        tabs = findViewById(R.id.tabs)

        fragStateAdapter = MainFragmentStateAdapter(this,fragList)
        viewPager.adapter = fragStateAdapter
        tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy {
            tab: TabLayout.Tab, position: Int -> tab.text  = tabTitles[position]
        }
        tabLayoutMediator = TabLayoutMediator(tabs, viewPager, tabConfigurationStrategy)
        tabLayoutMediator.attach()
    }


    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }

    /**
     * Helper function to reduce tab change sensitivity
     */
    private fun ViewPager2.reduceDragSensitivity(sens: Int = VIEWPAGER_SENSITIVITY) {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView
        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * sens)
    }
}