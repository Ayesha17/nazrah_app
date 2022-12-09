package com.nazrah.nazrahapp.fragments.auth

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
import android.util.Log
import  android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nazrah.nazrahapp.BuildConfig
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentSignupBinding
import com.nazrah.nazrahapp.utils.*
import com.nazrah.nazrahapp.utils.Constants.USERS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private lateinit var mBinding: FragmentSignupBinding

    private var currentFileUri: Uri? = null
    private var currentFilePath: String? = null
    private var storageRef: StorageReference? = null
    private var downloadUri:Uri?=null
    private var type:String?=null
    private var filePath:Uri?=null
    //permission launcher
    private var permissionsResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { success ->
            if (success.containsValue(true)) {
                chooseOrCaptureImage()
            }
        }

    //crop image launcher
    private var cropImage = registerForActivityResult(CameraCrop()) { result ->
        when {
            result.isSuccessful -> {
                filePath=Uri.parse(result.uriContent.toString())
                if (filePath != null) {
                    mBinding.ivProfilePic.loadImage(filePath)
                    uploadImage()
                }

            }
        }
    }

    // pick or capture image
    private var pickOrCaptureResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {//dont check for data != null because it will be null in camera case

                var imageUri = currentFileUri
                if (result.data != null && result?.data?.data != null)
                    imageUri = result.data?.data

                cropImage.launch(
                    imageUri?.let { setCropOption(it) }
                )
            }
        }

    override fun getFragmentLayout() = R.layout.fragment_signup

    override fun getViewBinding() {
        mBinding = binding as FragmentSignupBinding
    }

    override fun getViewModel() {
    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {
// creating a storage reference
        storageRef = FirebaseStorage.getInstance().reference
     val typeList=   arrayListOf(Constants.TEACHER, Constants.STUDENTS, Constants.OTHERS)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            typeList
        )
        // locationDropDownLayoutBinding.datesFilterSpinner.setText("All Types")
        mBinding.spType.setAdapter(adapter)
        mBinding.spType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                type=typeList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val user = Firebase.auth.currentUser

    }

    override fun setListeners() {
        mBinding.apply {
            ivProfilePic.setOnClickListener(this@SignUpFragment)
            btRegister.setOnClickListener(this@SignUpFragment)
         }
    }

    override fun setLanguageData() {

    }
    private fun uploadImage(){
        if(filePath != null){
            val ref = storageRef?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUri = task.result
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(requireContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivProfilePic -> {
                requestPermissions()
            }
            R.id.btRegister -> {

                if (mBinding.etEmail.text.toString().isEmpty()) {
                    requireContext().toastMessage("Please enter email ")
                } else if (mBinding.etPassword.text.toString().isEmpty()) {
                    requireContext().toastMessage("Please enter password ")
                } else if (type?.isEmpty()==true) {
                    requireContext().toastMessage("Please select any profession ")
                } else {
                    register()
                }
            }

        }
    }

    private fun register() {
        val email = mBinding.etEmail.text.toString()
        val password = mBinding.etPassword.text.toString()
        if (email.isNotBlank() && email.isNotEmpty() && password.isNotBlank() && password.isNotEmpty()) {
            FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        lifecycleScope.launch {
                            val profileUpdates = userProfileChangeRequest {
                                displayName = mBinding.etFullName.text.toString()
                                photoUri = downloadUri
//                                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                            }
                            task.result.user?.updateProfile(profileUpdates)?.await()
                            Log.e(SignUpFragment::class.java.simpleName, "")
                            storeInDb(task)
                        }
                    }
                }.addOnFailureListener { exception ->
                    requireContext().toastMessage(exception.localizedMessage!!.toString())
                }
        } else {
            requireContext().toastMessage("Invalid")
        }
    }

    private fun storeInDb(task: Task<AuthResult>) {
        val db = Firebase.firestore(Firebase.app)

        // Create a new user with a first and last name

        val user = hashMapOf(
            "user_id" to task.result.user?.uid,
            "type" to Constants.hashMap[type],
            "classes" to "",
            "classes_join" to ""
        )


        // Add a new document with a generated ID
        db.collection(USERS).document(task.result.user?.uid!!).set(user)
            .addOnSuccessListener { documentReference ->
                findNavController().navigate(R.id.homeFragment)
                Log.e(
                    SignUpFragment::class.simpleName,
                    "DocumentSnapshot added with ID: ${documentReference.toString()}"
                )
            }
            .addOnFailureListener { e ->
                Log.w(SignUpFragment::class.simpleName, "Error adding document", e)
                requireContext().toastMessage(e.message ?: "")

            }
    }

    private fun requestPermissions() {
        permissionsResultLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }

    private fun chooseOrCaptureImage() {
        if (hasPermissions(requireContext()).not()) {
            requestPermissions()

        } else {

            pickOrCaptureResultLauncher.launch(getPickImageIntent(requireContext()))
        }
    }

    private fun getPickImageIntent(context: Context): Intent? {
        var chooserIntent: Intent? = null
        var intentList: MutableList<Intent> = ArrayList()
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

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
            requireContext(),
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