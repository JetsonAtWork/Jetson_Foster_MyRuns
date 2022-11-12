  package com.example.jetson_foster.manual_entry

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.jetson_foster.R
import com.example.jetson_foster.structs.EntryField
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar

  class ManualEntryActivity : AppCompatActivity() {
    lateinit var entries: ListView

    override fun onCreate(save: Bundle?) {
        super.onCreate(save)
        val viewModel: ManualEntryViewModel by viewModels()
        viewModel.setActivity(intent.getIntExtra("activity_type",0))
        // region input field initialization
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(this,
            { _, year, month, day ->
                viewModel.setDate(year,month,day)
            }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_WEEK))
        val timePicker = TimePickerDialog(this,
            { _, hour, minute ->
                viewModel.setTime(hour,minute,Calendar.getInstance().get(Calendar.SECOND))
            },LocalTime.now().hour,LocalTime.now().minute,true)




        setContentView(R.layout.activity_manual_entry)
        entries = findViewById(R.id.entry_list)
        val entryAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            EntryField.values().map(EntryField::asString))
        entries.adapter = entryAdapter

        entries.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, child, position, id ->
            val field = EntryField.fromInt(position)
            val isComment = field == EntryField.COMMENT
            //TODO DOES THE TIME PICKER START TIME FREEZE DURING ON CREATE? OR DOES IT KEEP CHANGING
            when (field) {
                EntryField.DATE -> datePicker.show()
                EntryField.TIME-> timePicker.show()
                EntryField.DURATION, EntryField.DISTANCE, EntryField.CALORIES, EntryField.HEART_RATE, EntryField.COMMENT -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(field.asString)
                    val editText = EditText(this)
                    builder.setView(editText)
                    if (isComment) {
                        editText.inputType = android.text.InputType.TYPE_CLASS_TEXT
                        editText.hint = "How did it go? Notes here."
                    } else {
                        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER
                    }
                    builder.setPositiveButton("OK") { _, _ ->
                        if (isComment) {
                            viewModel.setComment(editText.text.toString())
                        } else {
                            viewModel.addDoubleDataByField(field,
                                editText.text.toString().toDouble())
                        }

                    }
                    builder.setNegativeButton("CANCEL") { dialog, _ ->
                        dialog.dismiss()
                    }
                    builder.show()
                }
            }
        }
        // endregion input field initialization
        // region saveButton
        val saveButton = findViewById<Button>(R.id.entry_save)
        saveButton.setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveEntry()
            }
            Toast.makeText(this,"Entry Saved",Toast.LENGTH_SHORT).show()
            finish()
        }
        // endregion saveButton
        // region cancelButton
        val cancelButton = findViewById<Button>(R.id.entry_cancel)
        cancelButton.setOnClickListener {
            Toast.makeText(this,"Entry Discarded",Toast.LENGTH_SHORT).show()
            finish()
        }
        // endregion cancelButton
    }
}
