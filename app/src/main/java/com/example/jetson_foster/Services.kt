package com.example.jetson_foster

import android.app.Activity
import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.preference.PreferenceManager
import java.io.File

object Services {
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE)
    /**
     * It is assumed that users will grant permissions so denial is undefined
     */
    fun getPermissions(activity: Activity) {
        if (permissions.any {s -> ActivityCompat.checkSelfPermission(activity,s) != PackageManager.PERMISSION_GRANTED}) {
            ActivityCompat.requestPermissions(activity, permissions, 0)
        }
    }

    fun getDistanceUnits(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(context.resources.getString(R.string.unit_system),"metric")!!
    }

    fun getImageUriFromFileName(activity: Activity,imageName: String): Uri {
        val picFile = getLocalFile(activity,imageName)
        return FileProvider.getUriForFile(activity,BuildConfig.APPLICATION_ID + ".provider",picFile)
    }

    fun getLocalFile(activity: Activity,fileName: String): File {
        return File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),fileName)
    }

    fun getBitmapFromFileName(activity: Activity, fileName: String): Bitmap {
        val picFile = getLocalFile(activity,fileName)
        return bitmapHelper(picFile.path)
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return uri.path?.let { bitmapHelper(it) }
    }
    //D/DEEEEBUG: /external/images/media/13081
    ///storage/emulated/0/Android/data/com.example.jetson_foster/files/Pictures/tmp_pic.png
    private fun bitmapHelper(pathString: String): Bitmap {
        Log.d("DEEEEBUG",pathString + " \n\n\n")
        var bitmap = BitmapFactory.decodeFile(pathString)
        val exif = ExifInterface(pathString)
        val orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
        when (orientation) {
            (ExifInterface.ORIENTATION_ROTATE_90.toString()) -> rotateImage(bitmap,90).also { bitmap = it }
            (ExifInterface.ORIENTATION_ROTATE_180.toString()) -> rotateImage(bitmap, 180).also { bitmap = it }
            (ExifInterface.ORIENTATION_ROTATE_270.toString()) -> rotateImage(bitmap,270).also { bitmap = it }
        }
        return bitmap
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }


}