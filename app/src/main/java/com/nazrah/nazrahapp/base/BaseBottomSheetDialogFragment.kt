package com.nazrah.nazrahapp.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.interfaces.InitMethods


internal abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), InitMethods {


    protected lateinit var className: String
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


        binding = DataBindingUtil.inflate(
            inflater,
            getFragmentLayout(),
            container,
            false
        )

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewBinding()

        init()

        setLanguageData()

        setLiveDataValues()

        setListeners()

    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState)

        return BottomSheetDialog(requireContext(), theme)
    }

}
