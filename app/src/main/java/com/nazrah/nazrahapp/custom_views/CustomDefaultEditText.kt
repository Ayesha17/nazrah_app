package com.nazrah.nazrahapp.custom_views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputType
import android.text.SpannableString
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.google.android.material.textfield.TextInputLayout
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.databinding.CustomDefaultEdittextBinding
import com.nazrah.nazrahapp.utils.getSafe
import com.nazrah.nazrahapp.utils.restoreChildViewStates
import com.nazrah.nazrahapp.utils.saveChildViewStates

class CustomDefaultEditText(context: Context, attrs: AttributeSet)
    : LinearLayout(context,  attrs) {

    private val defaultCharLength = 50

    val mBinding: CustomDefaultEdittextBinding =
        CustomDefaultEdittextBinding.inflate(LayoutInflater.from(context), this, true)

    var drawableStart: Int? = 0
        set(value) {
            field = value
//            if (value != 0)
//                mBinding.textInputLayout.setStartIconDrawable(value ?: 0)

        }


    var endIconMode: Int = 0
        set(value) {
            field = value
//            if (value != 0)
//                mBinding.textInputLayout.endIconMode = value
        }


    var drawable: Int? = 0
        set(value) {
            field = value
            if (value != 0) {
                if (endIconMode == TextInputLayout.END_ICON_CUSTOM) {
//                    mBinding.textInputLayout.setEndIconDrawable(value ?: 0)
                }
            }
        }

    var inputType: Int? = 0
        set(value) {
            field = value
            mBinding.editText.inputType = value.getSafe()

        }
    var drawableEnd: Int? = 0
        set(value) {
            field = value
//            if (value != 0)
//                mBinding.textInputLayout.setEndIconDrawable(value ?: 0)
        }

    var text: String = ""
        set(value) {
            field = value
            mBinding.editText.setText(SpannableString(value))
        }
        get() = mBinding.editText.text.toString()

    var hint: String = ""
        set(value) {
            field = value
//            mBinding.textInputLayout.hint = value
        }

    var errorText: String? = null
        set(value) {
            field = value
//            mBinding.textInputLayout.error = value
        }

    var click: Boolean = false
        set(value) {
            field = value
            if (click){
                mBinding.editText.isEnabled = true
                mBinding.editText.isClickable = true
                mBinding.editText.isFocusable = false
                mBinding.editText.isFocusableInTouchMode = false

                mBinding.editText.setOnClickListener {
                    clickCallback?.invoke()
                    return@setOnClickListener
                }
            }

        }


    var maxLength: Int? = 0
        set(value) {
            field = value
            if (value != 0)
                mBinding.editText.filters =
                    arrayOf<InputFilter>(InputFilter.LengthFilter(value ?: defaultCharLength))
        }

    var imeOptionCallback: (() -> Unit)? = null
    var clickCallback: (() -> Unit)? = null
    var onTextChangedListener: ((String) -> Unit)? = null

    init {
        val styledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.CustomDefaultEditText)

        drawableStart =
            styledAttributes.getResourceId(R.styleable.CustomDefaultEditText_drawableStart, 0)
        drawableEnd =
            styledAttributes.getResourceId(R.styleable.CustomDefaultEditText_drawableEnd, 0)
        text = styledAttributes.getString(R.styleable.CustomDefaultEditText_android_text).getSafe()
        errorText =
            styledAttributes.getString(R.styleable.CustomDefaultEditText_error)
                .getSafe()
        hint = styledAttributes.getString(R.styleable.CustomDefaultEditText_android_hint).getSafe()
        click = styledAttributes.getBoolean(R.styleable.CustomDefaultEditText_click,false)
        val digits = styledAttributes.getString(R.styleable.CustomDefaultEditText_android_digits).getSafe()

        maxLength = styledAttributes.getInt(
            R.styleable.CustomDefaultEditText_android_maxLength,
            defaultCharLength
        ).getSafe()
        inputType = styledAttributes.getInt(
            R.styleable.CustomDefaultEditText_android_inputType,
            InputType.TYPE_CLASS_TEXT
        ).getSafe()
        endIconMode = styledAttributes.getInt(
            R.styleable.CustomDefaultEditText_endIconMode,
            TextInputLayout.END_ICON_NONE
        ).getSafe()
        val imeOptions =
            styledAttributes.getInt(R.styleable.CustomDefaultEditText_android_imeOptions, 0)
                .getSafe()
        drawable =
            styledAttributes.getResourceId(R.styleable.CustomDefaultEditText_drawable, 0)

        mBinding.editText.imeOptions = imeOptions

        if(digits.isEmpty().not())
            mBinding.editText.keyListener = DigitsKeyListener.getInstance(digits)
        mBinding.editText.setRawInputType(inputType.getSafe())

        mBinding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == imeOptions) {
                imeOptionCallback?.invoke()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        if (errorText != null) {
            mBinding.editText.doAfterTextChanged {
                errorText = null
            }
        }
    }

    //////////// two way binding adapter ///////////////////

    @InverseBindingMethods(
        InverseBindingMethod(
            type = CustomDefaultEditText::class,
            attribute = "android:text",
            method = "getText"
        )
    )
    class CustomEditTextBinder {
        companion object {
            @JvmStatic
            @BindingAdapter(value = ["android:textAttrChanged"])
            fun setListener(editText: CustomDefaultEditText, listener: InverseBindingListener?) {
                if (listener != null) {
                    editText.mBinding.editText.doOnTextChanged { text, start, before, count ->
                        listener.onChange()
                    }
                }
            }

            @JvmStatic
            @BindingAdapter("android:text")
            fun setText(editText: CustomDefaultEditText, text: String?) {
                text?.let {
                    if (it != editText.text) {
                        editText.text = it
                    }
                }
            }
        }
    }

    //////////// two way binding end ///////////////////

    //////////// state management //////////////////////////

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            childrenStates = saveChildViewStates()
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        when (state){
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                state.childrenStates?.let { restoreChildViewStates(it) }
            }
            else -> super.onRestoreInstanceState(state)
        }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchThawSelfOnly(container)
    }

    internal class SavedState : BaseSavedState {

        var childrenStates: SparseArray<Parcelable>? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            childrenStates = source.readSparseArray<SparseArray<Parcelable>>(javaClass.classLoader) as SparseArray<Parcelable>?
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeSparseArray(childrenStates as SparseArray<Any>)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel) = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

    //////////// state management end //////////////////////////
}