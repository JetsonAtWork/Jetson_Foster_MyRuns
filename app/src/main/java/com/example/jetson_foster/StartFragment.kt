package com.example.jetson_foster

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jetson_foster.gps.GPSActivity
import com.example.jetson_foster.manual_entry.ManualEntryActivity
import com.example.jetson_foster.structs.ActivityType
import com.example.jetson_foster.structs.InputType

class StartFragment : Fragment() {
    lateinit var inputSpinner: Spinner
    lateinit var activitySpinner: Spinner
    lateinit var iSpinnerAdapter: SpinnerAdapter
    lateinit var aSpinnerAdapter: SpinnerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_start, container, false)
        // region spinners initialization
        activitySpinner = layout.findViewById(R.id.activity_spinner)
        inputSpinner = layout.findViewById(R.id.input_spinner)
        requireActivity().let {
            iSpinnerAdapter = ArrayAdapter(it,
                android.R.layout.simple_spinner_dropdown_item,
                InputType.values().map { s -> s.asString })
            inputSpinner.adapter = iSpinnerAdapter

            aSpinnerAdapter = ArrayAdapter(it,
                android.R.layout.simple_spinner_dropdown_item,
                ActivityType.values().map { s -> s.asString })
            activitySpinner.adapter = aSpinnerAdapter

        }
        // endregion spinners initialization
        layout.findViewById<Button>(R.id.start_button).setOnClickListener {
            val intent: Intent?
            when (InputType.fromInt(inputSpinner.selectedItemPosition)) {
                InputType.MANUAL -> {
                    intent = Intent(activity, ManualEntryActivity::class.java)
                    intent.putExtra("activity_type",activitySpinner.selectedItemPosition)
                }
                InputType.GPS -> {
                    intent = Intent(activity, GPSActivity::class.java)
                    intent.putExtra("is_automatic", false)
                    intent.putExtra("activity_type",activitySpinner.selectedItemPosition)
                }
                InputType.AUTOMATIC -> {
                    intent = Intent(activity, GPSActivity::class.java)
                    intent.putExtra("is_automatic", true)
                }

            }
            activity?.startActivity(intent)
        }
        return layout
    }
}