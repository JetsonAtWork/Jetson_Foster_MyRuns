package com.example.jetson_foster.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.jetson_foster.R
import com.example.jetson_foster.databinding.ActivityEntryRecordBinding
import com.example.jetson_foster.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch
import java.util.IdentityHashMap

class EntryRecordActivity : AppCompatActivity() {
    private var _binding: ActivityEntryRecordBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: EntryRecordViewModel
    private var recordId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recordId = intent.getIntExtra("selected_record_id",-1)
        viewModel = EntryRecordViewModel(application, recordId)
        _binding = ActivityEntryRecordBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                binding.recordActivityType.setText(uiState.activityStr)
                binding.recordInputType.setText(uiState.inputStr)
                binding.dateAndTime.setText(uiState.dateTimeStr)
                binding.recordDuration.setText(uiState.durationStr)
                binding.recordDistance.setText(uiState.distanceStr)
                binding.recordCalories.setText(uiState.calories)
                binding.recordHeartRate.setText(uiState.heartRate)
            }
        }
        setContentView(binding.root)
        setSupportActionBar(binding.recordToolbar)
        return
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                lifecycle.coroutineScope.launch {
                    viewModel.deleteEntry(recordId)
                    finish()
                    }
                }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


}