package com.nazrah.nazrahapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.nazrah.nazrahapp.BuildConfig
import com.nazrah.nazrahapp.R
import java.io.File
import java.util.ArrayList

class FileUtils(private val context: Context) {

    private var currentFileUri: Uri? = null
    private var currentFilePath: String? = null
    private lateinit var pickOrCaptureResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsResultLauncher: ActivityResultLauncher<Array<String>>
    private var successCallback: ((Uri?) -> Unit)? = null

    private lateinit var cropImage: ActivityResultLauncher<CropImageContractOptions>

    private var filePath: Uri? = null
    fun init(fragment: Fragment) {
        //permission launcher
        permissionsResultLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { success ->
                if (success.containsValue(true)) {
                    chooseOrCaptureImage(successCallback)
                }else{
                    successCallback?.invoke(null)
                }
            }

        //crop image launcher
        cropImage = fragment.registerForActivityResult(CameraCrop()) { result ->
            when {
                result.isSuccessful -> {
                    filePath = Uri.parse(result.uriContent.toString())
                    if (filePath != null) {
                        successCallback?.invoke(filePath)

                    }

                }
            }
        }

        // pick or capture image
        pickOrCaptureResultLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {//dont check for data != null because it will be null in camera case

                    var imageUri = currentFileUri
                    if (result.data != null && result?.data?.data != null)
                        imageUri = result.data?.data

                    cropImage.launch(
                        imageUri?.let { setCropOption(it) }
                    )
                }
            }
    }

    private fun chooseOrCaptureImage(mSuccessCallback: ((Uri?) -> Unit)?) {
        if (hasPermissions(context).not()) {
            requestPermissions(mSuccessCallback)

        } else {
            successCallback=mSuccessCallback
            pickOrCaptureResultLauncher.launch(getPickImageIntent(context))
        }
    }


    fun requestPermissions(callback:((Uri?) -> Unit)?=null) {
        successCallback=callback
        permissionsResultLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }


    private fun getPickImageIntent(context: Context): Intent? {
        var chooserIntent: Intent? = null
        var intentList: MutableList<Intent> = ArrayList()
        val pickIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = createImageFile()
        currentFileUri = getUriFromFile(file)
        currentFilePath = file.absolutePath
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentFileUri)

        intentList = addIntentsToList(context, intentList, pickIntent)
        intentList = addIntentsToList(context, intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                "Select an option"
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }

    private fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    private fun createImageFile(): File {
        val timeStamp = System.currentTimeMillis().toString()
        val storageDir: File =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        val fileName = "${timeStamp}.jpg"
        val file = File(storageDir, fileName)
        file.parentFile.mkdirs()
        file.createNewFile()
        return file.apply {
            currentFileUri = getUriFromFile(this)
            currentFilePath = absolutePath
        }

    }

    private fun getUriFromFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".fileprovider",
            file
        )
    }

    private fun hasPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun setCropOption(uri: Uri): CropImageContractOptions {
        return options(uri) {
            setScaleType(CropImageView.ScaleType.FIT_CENTER)
            setCropShape(CropImageView.CropShape.RECTANGLE)
            setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            setAspectRatio(1, 1)
            setMaxZoom(4)
            setAutoZoomEnabled(true)
            setMultiTouchEnabled(true)
            setCenterMoveEnabled(true)
            setShowCropOverlay(true)
            setAllowFlipping(true)
            setSnapRadius(3f)
            setTouchRadius(48f)
            setInitialCropWindowPaddingRatio(0.1f)
            setBorderLineThickness(3f)
            setBorderLineColor(Color.argb(170, 255, 255, 255))
            setBorderCornerThickness(2f)
            setBorderCornerOffset(5f)
            setBorderCornerLength(14f)
            setBorderCornerColor(Color.WHITE)
            setGuidelinesThickness(1f)
            setGuidelinesColor(R.color.white)
            setBackgroundColor(Color.argb(119, 0, 0, 0))
            setMinCropWindowSize(24, 24)
            setMinCropResultSize(20, 20)
            setMaxCropResultSize(99999, 99999)
            setActivityTitle("")
            setActivityMenuIconColor(0)
            setOutputUri(null)
            setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
            setOutputCompressQuality(90)
            setRequestedSize(0, 0)
            setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
            setInitialCropWindowRectangle(null)
//            setInitialRotation(0)
            setAllowCounterRotation(false)
            setFlipHorizontally(false)
            setFlipVertically(false)
            setCropMenuCropButtonTitle(null)
            setCropMenuCropButtonIcon(0)
            setAllowRotation(false)
            setNoOutputImage(false)
            setFixAspectRatio(true)
        }
    }
}