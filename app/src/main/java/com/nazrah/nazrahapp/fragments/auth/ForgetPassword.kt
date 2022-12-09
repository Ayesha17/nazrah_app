package com.nazrah.nazrahapp.fragments.auth

import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentForgetpwdBinding
import com.nazrah.nazrahapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPassword : BaseFragment() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var mBinding: FragmentForgetpwdBinding

    override fun getFragmentLayout(): Int = R.layout.fragment_forgetpwd

    override fun getViewBinding() {
        mBinding = binding as FragmentForgetpwdBinding

    }

    override fun getViewModel() {

    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {
        mAuth = FirebaseAuth.getInstance()

    }

    override fun setListeners() {
        mBinding.apply {
            btnConfirm.setOnClickListener(this@ForgetPassword)
            ivBack.setOnClickListener(this@ForgetPassword)
        }
    }

    override fun setLanguageData() {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnConfirm -> {
                if (mBinding.etEmail.text.isEmpty().not()) {
                    mAuth!!.sendPasswordResetEmail(mBinding.etEmail.text)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) { // successful!
                                Toast.makeText(requireContext(), "Email sent!", Toast.LENGTH_SHORT)
                                    .show()
                            } else { // failed!
                                Toast.makeText(
                                    requireContext(),
                                    "Email sent failed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Please enter email", Toast.LENGTH_SHORT)
                        .show()

                }
            }
            R.id.ivBack->{
                findNavController().popBackStack()
            }
        }
    }
}