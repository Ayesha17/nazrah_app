package com.nazrah.nazrahapp.fragments.auth

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.adapters.TabsPagerAdapter
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentAuthBinding
import com.nazrah.nazrahapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment() {
    private lateinit var mBinding: FragmentAuthBinding

    override fun getFragmentLayout(): Int = R.layout.fragment_auth


    override fun getViewBinding() {
        mBinding = binding as FragmentAuthBinding
    }

    override fun getViewModel() {

    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {
        setView()
    }

    override fun setListeners() {

    }

    override fun setLanguageData() {

    }

    override fun onClick(v: View?) {

    }

    private fun setView() {
        mBinding.apply {
            TabsPagerAdapter.tabCount=2
            viewPager.adapter = TabsPagerAdapter(childFragmentManager, lifecycle)
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.login)
                    1 -> getString(R.string.signup)
                    else -> getString(R.string.login)
                }
            }.attach()
        }
    }
}