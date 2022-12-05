package com.nazrah.nazrahapp.custom_views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.databinding.CustomTextViewBinding
import com.nazrah.nazrahapp.utils.getSafe

class CustomTextView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val mBinding: CustomTextViewBinding =
        CustomTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    var titleText: String = ""
        set(value) {
            field = value
            mBinding.tvTitle.text = value

        }

    var isSingleLine: Boolean = false
        set(value) {
            field = value
            mBinding.tvTitle.isSingleLine = value
            mBinding.tvTitle.ellipsize = TextUtils.TruncateAt.END

        }
    var textColor: Int = 0
        set(value) {
            field = value
            mBinding.tvTitle.setTextColor(value)
        }

    var textStyle: Int = 0
        set(value) {
            field = value
            mBinding.tvTitle.setTextAppearance(context, value)
        }


    init {
        val styledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)

        titleText = styledAttributes.getString(R.styleable.CustomTextView_titleText).getSafe()
        isSingleLine = styledAttributes.getBoolean(R.styleable.CustomTextView_isSingleLine, false)

        textColor = styledAttributes.getInt(R.styleable.CustomTextView_textColorRes, 0)
        textStyle = styledAttributes.getResourceId(R.styleable.CustomTextView_textStyle, 0)
    }
}