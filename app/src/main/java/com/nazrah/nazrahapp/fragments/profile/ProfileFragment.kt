package com.nazrah.nazrahapp.fragments.profile

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.UploadTask
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.adapters.ProfileAdapter
import com.nazrah.nazrahapp.adapters.ProfileItemClickListener
import com.nazrah.nazrahapp.adapters.ViewPagerAdapter
import com.nazrah.nazrahapp.adapters.ViewPagerClickListener
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentProfileBinding
import com.nazrah.nazrahapp.models.ProfileItem
import com.nazrah.nazrahapp.models.ViewPagerItem
import com.nazrah.nazrahapp.preferences.Preferences
import com.nazrah.nazrahapp.utils.CameraCrop
import com.nazrah.nazrahapp.utils.FileUtils
import com.nazrah.nazrahapp.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var mBinding: FragmentProfileBinding

    private var storageRef: StorageReference? = null
    private var fileUtils: FileUtils? = null

    @Inject
    lateinit var preferences: Preferences
    private var downloadUri: Uri? = null
    private var filePath: Uri? = null

    override fun getFragmentLayout() = R.layout.fragment_profile

    override fun getViewBinding() {
        mBinding = binding as FragmentProfileBinding
    }

    override fun getViewModel() {
    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {
        fileUtils = FileUtils(requireContext())
        fileUtils?.init(this)
        mBinding.rvProfileList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rvProfileList.adapter = ProfileAdapter(ProfileItemClickListener {
            when (it.id) {
                4 -> {
                    Firebase.auth.signOut()
                    findNavController().navigate(R.id.action_profileFragment_to_authFragment)
                }
            }
        }, getList())

    }

    private fun getList(): ArrayList<ProfileItem> {
        val profileList: ArrayList<ProfileItem> = arrayListOf()
        profileList.add(ProfileItem(1, R.drawable.ic_person, "About us"))
        profileList.add(ProfileItem(2, R.drawable.ic_person, "Scholarship"))
        profileList.add(ProfileItem(3, R.drawable.ic_lock, "Change Password"))
        profileList.add(ProfileItem(4, R.drawable.ic_lock, "Logout"))
        return profileList
    }

    override fun setListeners() {
        mBinding.ivAddImage.setOnClickListener(this)

    }

    override fun setLanguageData() {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivAddImage -> {
                fileUtils?.requestPermissions {
                    filePath = it
                    mBinding.ivUserimage.loadImage(filePath)
                    uploadImage()
                }
            }
        }

    }

    private fun uploadImage() {
        if (filePath != null) {
            val index = preferences.user?.profilePic?.lastIndexOf('/')
            val picTitle =
                if (index == -1) preferences.user?.profilePic else preferences.user?.profilePic?.substring(
                    (index ?: 0) + 2,
                    preferences.user?.profilePic?.length ?: 0
                )
            val ref = storageRef?.child("uploads/$picTitle")
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

}