package com.nazrah.nazrahapp.fragments.auth

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
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
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentSignupBinding
import com.nazrah.nazrahapp.models.UserData
import com.nazrah.nazrahapp.preferences.Preferences
import com.nazrah.nazrahapp.utils.*
import com.nazrah.nazrahapp.utils.Constants.USERS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private lateinit var mBinding: FragmentSignupBinding

    @Inject
    lateinit var preferences: Preferences
    private var downloadUri: Uri? = null
    private var type: String? = null
    private var filePath: Uri? = null
    private var storageRef: StorageReference? = null
    private var fileUtils: FileUtils? = null
    //   var mAuthListener = object: FirebaseAuth.AuthStateListener  {
//       override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
//           var user = firebaseAuth.getCurrentUser();
//           if (user != null) {
//               // User is signed in
//               // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
//               // the user will receive another verification email.
//               sendVerificationEmail();
//           } else {
//               // User is signed out
//
//           }
//       }
//      }

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
        val typeList = arrayListOf(Constants.TEACHER, Constants.STUDENTS, Constants.OTHERS)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            typeList
        )
        fileUtils = FileUtils(requireContext())
        fileUtils?.init(this)
        // locationDropDownLayoutBinding.datesFilterSpinner.setText("All Types")
        mBinding.spType.setAdapter(adapter)
        mBinding.spType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                type = typeList[position]
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

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageRef?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
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
                }?.addOnFailureListener {

                }
        } else {
            Toast.makeText(requireContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivProfilePic -> {
                fileUtils?.requestPermissions {
                    filePath = it
                    mBinding.ivProfilePic.loadImage(filePath)
                    uploadImage()
                }
            }
            R.id.btRegister -> {

                if (mBinding.etEmail.text.isEmpty()) {
                    requireContext().toastMessage("Please enter email ")
                } else if (mBinding.etPassword.text.isEmpty()) {
                    requireContext().toastMessage("Please enter password ")
                } else if (type?.isEmpty() == true) {
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

//            val actionCodeSettings = actionCodeSettings {
//                // URL you want to redirect back to. The domain (www.example.com) for this
//                // URL must be whitelisted in the Firebase Console.
//                url = "https://www.example.com/finishSignUp?cartId=1234"
//                // This must be true
//                handleCodeInApp = true
//                setIOSBundleId("com.example.ios")
//                setAndroidPackageName(
//                    "com.nazrah.nazrahapp",
//                    true, /* installIfNotAvailable */
//                    "22" /* minimumVersion */)
//            }
//            Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                       Timber.e( "Email sent.")
//                    }
//                }

            FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebaseUtils.firebaseAuth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener(object : OnCompleteListener<Void> {


                                override fun onComplete(p0: Task<Void>) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(
                                            requireContext(),
                                            "please check email for verification.",
                                            Toast.LENGTH_SHORT
                                        ).show();
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
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            task.exception.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }
                            })
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
            .addOnSuccessListener {
                preferences.user = UserData(
                    task.result.user?.uid,
                    mBinding.etFullName.text,
                    downloadUri.toString(),
                    mBinding.etEmail.text,
                    Constants.hashMap[type].toString(),
                    "",
                    ""
                )

                DialogUtil(requireContext())
                    .showDoubleButtonsAlertDialog(
                        title = getString(R.string.successTitle),
                        message = getString(R.string.registerSuccess),
                        positiveButtonStringText = getString(R.string.ok),
                        positivebuttonCallback = {
                            findNavController().navigate(R.id.homeFragment)
                        },
                    )


            }
            .addOnFailureListener { e ->
                Log.w(SignUpFragment::class.simpleName, "Error adding document", e)
                requireContext().toastMessage(e.message ?: "")

            }
    }

//    private fun sendVerificationEmail() {
//        val user = FirebaseAuth.getInstance().currentUser
//        user!!.sendEmailVerification()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // email sent
//
//
//                    // after email is sent just logout the user and finish this activity
//                    FirebaseAuth.getInstance().signOut()
//                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
//                    finish()
//                } else {
//                    // email not sent, so display message and restart the activity or do whatever you wish to do
//
//                    //restart this activity
//                    overridePendingTransition(0, 0)
//                    finish()
//                    overridePendingTransition(0, 0)
//                    startActivity(getIntent())
//                }
//            }
//    }
}