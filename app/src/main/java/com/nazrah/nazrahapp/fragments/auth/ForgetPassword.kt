package com.nazrah.nazrahapp.fragments.auth

import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment

class ForgetPassword : BaseFragment() {
    private var mAuth: FirebaseAuth? = null
    override fun getFragmentLayout(): Int = R.layout.fragment_splash

    override fun getViewBinding() {

    }

    override fun getViewModel() {

    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {
        mAuth = FirebaseAuth.getInstance()
        mAuth!!.sendPasswordResetEmail("email").addOnCompleteListener { task ->
            if (task.isSuccessful) { // successful!
            } else { // failed!
            }
        }
    }

    override fun setListeners() {

    }

    override fun setLanguageData() {

    }

    override fun onClick(p0: View?) {

    }
}