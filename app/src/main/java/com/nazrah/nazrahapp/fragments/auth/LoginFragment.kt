package com.nazrah.nazrahapp.fragments.auth

import android.view.View
import androidx.navigation.fragment.findNavController
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentLoginBinding
import com.nazrah.nazrahapp.utils.FirebaseUtils
import com.nazrah.nazrahapp.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var mBinding: FragmentLoginBinding

    override fun getFragmentLayout() = R.layout.fragment_login

    override fun getViewBinding() {
        mBinding = binding as FragmentLoginBinding
    }

    override fun getViewModel() {
    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {

    }

    override fun setListeners() {
        mBinding.apply {
            btnLogin.setOnClickListener(this@LoginFragment)

            tvForgetPassword.setOnClickListener(this@LoginFragment)
        }
    }

    override fun setLanguageData() {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                login()
            } R.id.tvForgetPassword -> {
               findNavController().navigate(R.id.action_loginFragment_to_forgetPassword)
            }
        }
    }

    private fun login() {
        val email = mBinding.etEmail.text.toString()
        val password = mBinding.etPassword  .text.toString()

        if (email.isNotBlank() && email.isNotEmpty() && password.isNotBlank() && password.isNotEmpty()) {
            FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.homeFragment)
                }
            }.addOnFailureListener { exception ->
                requireContext().toastMessage(exception.localizedMessage)
            }
        } else {
            requireContext().toastMessage("Invalid")
        }
    }
}