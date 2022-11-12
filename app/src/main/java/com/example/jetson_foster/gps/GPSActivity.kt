package com.example.jetson_foster.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.example.jetson_foster.R

class GPSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: GPSViewModel by viewModels()
        val isInferActivity = intent.getBooleanExtra("is_automatic",true)
        setContentView(R.layout.activity_gpsactivity)

        val saveButton = findViewById<Button>(R.id.gps_save)
        saveButton.setOnClickListener {
            // TODO save gps entry
            Toast.makeText(this,"Entry Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
        val cancelButton = findViewById<Button>(R.id.gps_cancel)
        cancelButton.setOnClickListener {
            Toast.makeText(this,"Entry Discarded",Toast.LENGTH_SHORT).show()
            finish()

        }
    }
}