package com.nazrah.nazrahapp.custom_views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.databinding.UserDropdownLayoutBinding

class UserDropDownCustomView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleRes) {


    private lateinit var userDropdownLayoutBinding: UserDropdownLayoutBinding

    init {
        init(attributeSet)
    }

    private fun init(attributeSet: AttributeSet?) {

        userDropdownLayoutBinding =
            UserDropdownLayoutBinding.inflate(LayoutInflater.from(context), this, false)
        addView(userDropdownLayoutBinding.root)

    }

    //////////// state management //////////////////////////

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            childrenStates = saveChildViewStates()
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        when (state) {
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
            childrenStates =
                source.readSparseArray<SparseArray<Parcelable>>(javaClass.classLoader) as SparseArray<Parcelable>?
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


    //for custom view state mgt
    fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
        val childViewStates = SparseArray<Parcelable>()
        children.forEach { child -> child.saveHierarchyState(childViewStates) }
        return childViewStates
    }

    //for custom view state mgt
    fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
        children.forEach { child -> child.restoreHierarchyState(childViewStates) }
    }

    //////////// state management end //////////////////////////

}