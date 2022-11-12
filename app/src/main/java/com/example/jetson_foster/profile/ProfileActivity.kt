package com.example.jetson_foster.profile

import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.jetson_foster.R
import com.example.jetson_foster.Services
import com.example.jetson_foster.structs.Source
import java.io.File

// TODO: MAKE TEMP FILES/URI's  FOR PICTURES TAKEN SO THEY DON"T CLUTTER MEMORY
class ProfileActivity : AppCompatActivity() {
    private val PIC_DIALOG_OPTIONS = arrayOf("Open Camera","Select from Gallery")
    private val PIC_SOURCE_PREF = "pic_source"
    private val TEMP_PIC_NAME = "tmp_pic.png"
    private val SAVED_PIC_NAME = "curr_pic.png"
    private lateinit var profilePic : ImageView
    private lateinit var inputName : EditText
    private lateinit var inputEmail : EditText
    private lateinit var inputPhone : EditText
    private lateinit var inputGender : RadioGroup
    private lateinit var inputClass : EditText
    private lateinit var inputMajor : EditText
    private lateinit var profUri: Uri
    private lateinit var prefs  : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val pictureResult = registerForActivityResult(PictureContract()) { success: Boolean ->
        if (success) {
            profilePic.setImageURI(profUri)
            editor.putInt(PIC_SOURCE_PREF, Source.TEMPORARY.asInt)
            editor.apply()
        }
    }
    /*private val photoPickerResult = registerForActivityResult(PickVisualMedia()) {
            profilePic.setImageBitmap(it?.let {  uri: Uri ->
                editor.putInt(PIC_SOURCE_PREF,Source.TEMPORARY.asInt)
                editor.apply()
                Services.getBitmapFromUri(uri)
             })
    }*/
    private val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let {
                profilePic.setImageURI(it)
                profUri = it
                editor.putInt(PIC_SOURCE_PREF, Source.TEMPORARY.asInt)
                editor.apply()
            }
        }
    }

    override fun onCreate(save: Bundle?) {
        super.onCreate(save)
        setContentView(R.layout.profile)

        profilePic = findViewById(R.id.profilePic)
        inputName = findViewById(R.id.edit_prof_name)
        inputEmail = findViewById(R.id.edit_prof_email)
        inputPhone = findViewById(R.id.edit_prof_phone)
        inputGender = findViewById(R.id.gender_select)
        inputClass = findViewById(R.id.edit_prof_class)
        inputMajor = findViewById(R.id.edit_prof_major)
        prefs = getPreferences(MODE_PRIVATE)
        editor = prefs.edit()

        Services.getPermissions(this)

        if (save != null) {
            profUri = save.getParcelable("prof_uri")!!
        } else {
            profUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + resources.getResourcePackageName(R.drawable.profile)
                    + '/' + resources.getResourceTypeName(R.drawable.profile)
                    + '/' + resources.getResourceEntryName(R.drawable.profile) )
                //Uri.parse("android.resource://com.example.jetson_foster/drawable/profile.png")
        }
        val sourceType = Source.fromInt(prefs.getInt(PIC_SOURCE_PREF, Source.DEFAULT.asInt))
        println("" + sourceType)
        when (sourceType) {
            Source.TEMPORARY -> profilePic.setImageURI(profUri)
            Source.STORED -> profilePic.setImageURI(Services.getImageUriFromFileName(this,
                SAVED_PIC_NAME))
            Source.DEFAULT -> profilePic.setImageURI(profUri)
        }
        Log.d("DEBUG","PROF URI IS" + profUri.path + "\n\n\n\n\n")
        loadProfile()
    }





    private fun loadProfile() {
        inputName.setText(prefs.getString(R.string.preference_name.toString(),""))
        inputEmail.setText(prefs.getString(R.string.preference_email.toString(),""))
        inputPhone.setText(prefs.getString(R.string.preference_phone.toString(),""))
        val genderPrefID = Integer.parseInt(prefs.getString(R.string.preference_gender.toString(),"-1"))
        if (genderPrefID == -1) {
            inputGender.clearCheck()
        } else {
            inputGender.check(genderPrefID)
        }
        inputClass.setText(prefs.getString(R.string.preference_class.toString(),""))
        inputMajor.setText(prefs.getString(R.string.preference_major.toString(),""))

    }

    fun saveProfile() {
        editor.putString(R.string.preference_name.toString(), inputName.text.toString())
        editor.putString(R.string.preference_email.toString(), inputEmail.text.toString())
        editor.putString(R.string.preference_phone.toString(), inputPhone.text.toString())
        editor.putString(R.string.preference_gender.toString(), inputGender.checkedRadioButtonId.toString())
        editor.putString(R.string.preference_class.toString(), inputClass.text.toString())
        editor.putString(R.string.preference_major.toString(), inputMajor.text.toString())
        if (prefs.getInt(PIC_SOURCE_PREF, Source.DEFAULT.asInt) == Source.TEMPORARY.asInt) {
            val inputStream = contentResolver.openInputStream(profUri)
            inputStream?.copyTo(
                Services.getLocalFile(this, SAVED_PIC_NAME)
                    .outputStream())
            /*val savedPicFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),SAVED_PIC_NAME)
            tmpPicFile.copyTo(savedPicFile,true)*/
            editor.putInt(PIC_SOURCE_PREF, Source.STORED.asInt)
        }
        editor.apply()

    }

    private fun takePicture() {
        profUri = Services.getImageUriFromFileName(this, TEMP_PIC_NAME)
        pictureResult.launch(profUri)
    }

    private fun selectFromGallery() {
       /* if (isPhotoPickerAvailable()) {
            //photoPickerResult.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }*/
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)

    }

    fun onChangeClick(view: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Profile Picture ")
        builder.setItems(PIC_DIALOG_OPTIONS,DialogInterface.OnClickListener { dialogInterface, i ->
            when (i) {
                0 ->  takePicture()
                1 -> selectFromGallery()
            }
        })
        builder.create().show()
    }

    fun onSaveClick(view: View) {
        println("debug: save button clicked")
        saveProfile()
        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
        finish()
    }



    fun onCancelClick(view: View) {
        if (File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),SAVED_PIC_NAME).exists()) {
            editor.putInt(PIC_SOURCE_PREF, Source.STORED.asInt)
        } else {
            editor.putInt(PIC_SOURCE_PREF, Source.DEFAULT.asInt)
        }
        editor.apply()
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("prof_uri",profUri)


    }
}