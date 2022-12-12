package com.nazrah.nazrahapp.fragments.profile

import android.view.View
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var mBinding: FragmentProfileBinding

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

    }

    override fun setListeners() {


    }

    override fun setLanguageData() {

    }

    override fun onClick(view: View?) {


    }
}