package com.example.jetson_foster.profile

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Binder
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ContentInfoCompat

class PictureContract: ActivityResultContracts.TakePicture() {
    override fun createIntent(context: Context, input: Uri): Intent {
        val intent = super.createIntent(context, input).setFlags(FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION or FLAG_GRANT_PERSISTABLE_URI_PERMISSION or FLAG_GRANT_PREFIX_URI_PERMISSION)
        println(input.path)
        println(intent.flags.toString())
        return intent
    }
}