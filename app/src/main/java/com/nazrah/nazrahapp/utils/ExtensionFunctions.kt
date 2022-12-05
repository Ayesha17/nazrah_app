package com.nazrah.nazrahapp.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.Settings
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.nazrah.nazrahapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.random
import java.util.*


fun Context.openNotificationSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
//for custom view state mgt
fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}
//for custom view state mgt
fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

fun String?.getSafe(): String {
    return this ?: ""
}

fun Fragment.changeStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        val window = this.requireActivity().window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(color)

    }
}


fun String?.getFirstWord(): String? {
    var firstLetter = ""
    return if (!this.isNullOrEmpty()) {
        this[0].toString()
    } else null
}

fun View.setVisible(show: Boolean = true) {
    if (show) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun Double.convert(): Long {
    return this.toLong()
}

fun String.convert(): Long {
    return this.toLong()
}


fun ImageView.loadImage(image: String?) {
    Glide.with(this.context)
        .load(image)
        .into(this)
}
fun ImageView.loadImage(image: Uri?) {
    var requestOptions = RequestOptions()
    requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(R.dimen.dp24))

    Glide.with(this.context)
        .load(image)
        .apply(requestOptions)
        .into(this)
}
fun View.setVisible(show: Boolean = true, invisible: Boolean = false) {
    if (show) this.visibility = View.VISIBLE
    else this.visibility = if(invisible) View.INVISIBLE else View.GONE
}

fun Context.checkBluetoothPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.BLUETOOTH
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkCalendarPermission(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_CALENDAR
    ) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CALENDAR
    ) == PackageManager.PERMISSION_GRANTED)
}

fun Context.toastMessage(msg: String) {
    GlobalScope.launch {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@toastMessage, msg, Toast.LENGTH_LONG).show()
        }
    }

}

fun String?.capWords(): String? {
    return if (this != null && this.isNotEmpty()) {
        val strArray = this.split(" ").toTypedArray()
        val builder = StringBuilder()
        for (s in strArray) {
            val cap = s.substring(0, 1).uppercase(Locale.getDefault()) + s.substring(1)
            builder.append("$cap ")
        }
        builder.toString()
    } else {
        this
    }
}

fun Int?.getSafe(): Int {
    return this ?: 0
}

fun Boolean?.getSafe(): Boolean {
    return this ?: false
}

fun setImage(iv: ImageView, image: String, placeholderRes: Int = 0) {

    Glide.with(iv.context)
        .load(image)
        .placeholder(placeholderRes)
        .fitCenter()
        .into(iv)

}

fun sixDigitsCode():Long{
   return 900000 + (random() * (100000 - 900000)).toLong()

}
