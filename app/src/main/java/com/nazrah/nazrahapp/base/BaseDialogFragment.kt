package com.nazrah.nazrahapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.interfaces.InitMethods


internal abstract class BaseDialogFragment : DialogFragment(), InitMethods {


    protected lateinit var className: String
    protected lateinit var binding: ViewDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.AppDialogTheme)
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

    override fun onResume() {
        super.onResume()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        val height = LinearLayout.LayoutParams.WRAP_CONTENT

        dialog?.window?.setLayout(width, height)

    }

}
