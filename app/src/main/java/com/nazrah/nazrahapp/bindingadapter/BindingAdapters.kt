package com.nazrah.nazrahapp.bindingadapter

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("disableLayout")
    fun ConstraintLayout.disableLayout(eligible: Boolean?) {
        this.apply {
            if (eligible?.not() == true) {
                this.isEnabled = false
                this.isClickable = false
            }
        }
    }

}