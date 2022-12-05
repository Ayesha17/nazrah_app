package com.nazrah.nazrahapp.fragments.splash

import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.adapters.ViewPagerAdapter
import com.nazrah.nazrahapp.adapters.ViewPagerClickListener
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentWalkthroughBinding
import com.nazrah.nazrahapp.models.ViewPagerItem

class WalkthroughFragment : BaseFragment(), TabLayoutMediator.TabConfigurationStrategy {
    private lateinit var mBinding: FragmentWalkthroughBinding

    override fun getFragmentLayout() = R.layout.fragment_walkthrough

    override fun getViewBinding() {
        mBinding = binding as FragmentWalkthroughBinding
    }

    override fun getViewModel() {
    }

    override fun observe() {

    }

    override fun setLiveDataValues() {

    }

    override fun init() {
        setupViewPager()
    }

    override fun setListeners() {

    }

    override fun setLanguageData() {

    }

    override fun onClick(p0: View?) {

    }


    private fun setupViewPager() {
        mBinding.pager.adapter = ViewPagerAdapter(ViewPagerClickListener {
            setViewPagerCurrentItem(mBinding.pager.currentItem)
        }, getPagerData())

        TabLayoutMediator(mBinding.tabLayout, mBinding.pager, this).attach()
    }

    private fun getPagerData(): MutableList<ViewPagerItem?> {
        return mutableListOf<ViewPagerItem?>().apply {
            this.add(
                ViewPagerItem(
                    R.drawable.emoji,
                    getString(R.string.slide1_title),
                    getString(R.string.slide1_Desc)
                )
            )
            this.add(
                ViewPagerItem(
                    R.drawable.person,
                    getString(R.string.slide2_title),
                    getString(R.string.slide2_Desc)
                )
            )
            this.add(
                ViewPagerItem(
                    R.drawable.star,
                    getString(R.string.slide3_title),
                    getString(R.string.slide3_Desc)
                )
            )
        }
    }


    private fun setViewPagerCurrentItem(currentItem: Int?) {
        when (currentItem) {
            0, 1 -> {
                mBinding.pager.setCurrentItem(currentItem.inc(), true)
            }
            else -> {
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) = Unit

}
