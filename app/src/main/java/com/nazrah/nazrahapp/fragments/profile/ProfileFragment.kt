package com.nazrah.nazrahapp.fragments.profile

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.adapters.ProfileAdapter
import com.nazrah.nazrahapp.adapters.ProfileItemClickListener
import com.nazrah.nazrahapp.adapters.ViewPagerAdapter
import com.nazrah.nazrahapp.adapters.ViewPagerClickListener
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentProfileBinding
import com.nazrah.nazrahapp.models.ProfileItem
import com.nazrah.nazrahapp.models.ViewPagerItem
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        mBinding.rvProfileList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rvProfileList.adapter = ProfileAdapter(ProfileItemClickListener {
            Timber.e("eror ${it.title}")
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


    }

    override fun setLanguageData() {

    }

    override fun onClick(view: View?) {


    }
}