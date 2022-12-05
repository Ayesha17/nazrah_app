package com.nazrah.nazrahapp.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.nazrah.nazrahapp.MainActivity
import com.nazrah.nazrahapp.interfaces.InitMethods

abstract class BaseFragment : Fragment(), InitMethods {
    private lateinit var className: String
    protected lateinit var binding: ViewDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        className = javaClass.name

        getViewModel()

        observe()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        try {
            binding = DataBindingUtil.inflate(
                inflater,
                getFragmentLayout(),
                container,
                false
            )

            return binding.root
        } catch (e: Exception) {
            e.message?.let { Log.e("fragment", it) }
        }
        return null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            getViewBinding()

            init()

            setLanguageData()

            setLiveDataValues()

            setListeners()

        } catch (exception: Exception) {

            val intent = Intent(requireContext(), MainActivity::class.java)
            requireActivity().finish()
            startActivity(intent)
            exception.message?.let { Log.e("restart_exception", it) }
        }

    }
}