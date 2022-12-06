package com.nazrah.nazrahapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nazrah.nazrahapp.fragments.auth.LoginFragment
import com.nazrah.nazrahapp.fragments.auth.SignUpFragment

class TabsPagerAdapter(fm: FragmentManager, lifeCycle: Lifecycle): FragmentStateAdapter(fm, lifeCycle) {
    companion object{
          var tabCount:Int = 0
    }

    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return LoginFragment()
            1 -> return SignUpFragment()
            else-> return LoginFragment()
        }
    }
}