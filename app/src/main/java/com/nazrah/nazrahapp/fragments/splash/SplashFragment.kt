package com.nazrah.nazrahapp.fragments.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentSplashBinding
import com.nazrah.nazrahapp.databinding.FragmentWalkthroughBinding
import com.nazrah.nazrahapp.preferences.Preferences
import com.nazrah.nazrahapp.utils.Constants
import com.nazrah.nazrahapp.utils.FirebaseUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private lateinit var fragmentSplashBinding: FragmentSplashBinding

    override fun getFragmentLayout() = R.layout.fragment_splash

    @Inject
    lateinit var preferences: Preferences
    override fun getViewBinding() {
        fragmentSplashBinding = binding as FragmentSplashBinding
    }

    override fun getViewModel() {

    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {
        lifecycleScope.launch {
            delay(5000)
            if (FirebaseUtils.firebaseAuth.currentUser == null) {
                if (!preferences.firstTimeLaunch)
                findNavController().navigate(R.id.walkthroughFragment)
               else
                   findNavController().navigate(R.id.authFragment)
            } else {
                Timber.e("Constants.USERS name " + FirebaseUtils.firebaseAuth.currentUser!!.displayName)
                Timber.e("Constants.USERS phoneNumber " + FirebaseUtils.firebaseAuth.currentUser!!.phoneNumber)
                Timber.e("Constants.USERS photoUrl " + FirebaseUtils.firebaseAuth.currentUser!!.photoUrl)
                Timber.e("Constants.USERS email " + FirebaseUtils.firebaseAuth.currentUser!!.email)
                val db = Firebase.firestore(Firebase.app)
                db.collection(Constants.USERS)
                    .document(FirebaseUtils.firebaseAuth.currentUser?.uid!!).get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Timber.e(
                                "Constants.USERS type " + it.result.get("type")
                            )
                        }
                    }.addOnFailureListener {
                        Timber.e("exception " + it.message)
                    }
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    override fun setListeners() {

    }

    override fun setLanguageData() {

    }

    override fun onClick(v: View?) {

    }


}