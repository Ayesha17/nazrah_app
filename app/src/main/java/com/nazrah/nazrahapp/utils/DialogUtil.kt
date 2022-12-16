package com.nazrah.nazrahapp.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.nazrah.nazrahapp.R

class DialogUtil(private val activity: Context) {
    fun showDoubleButtonsAlertDialog(
        title: String = "",
        message: String = "",
        positiveButtonText: Int = android.R.string.ok,
        negativeButtonText: Int = android.R.string.cancel,
        negativeButtonStringText: String = "",
        positiveButtonStringText: String = "",
        positivebuttonCallback: () -> Unit = {},
        negativeButtonCallback: () -> Unit = {},
        cancellable: Boolean = true
    ) {

        val positiveClickListener = DialogInterface.OnClickListener { dialog, _ ->
            positivebuttonCallback()
            dialog.dismiss()
        }
        val negButtonClickListener = DialogInterface.OnClickListener { dialog, _ ->
            negativeButtonCallback()
            dialog.dismiss()
        }
        val builder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)

        if (positiveButtonStringText.isNotEmpty())
            builder.setPositiveButton(positiveButtonStringText, positiveClickListener)
        else
            builder.setPositiveButton(positiveButtonText, positiveClickListener)
        if (positiveButtonStringText.isNotEmpty())
            builder.setNegativeButton(negativeButtonStringText, negButtonClickListener)
        else
            builder.setNegativeButton(negativeButtonText, negButtonClickListener)
        builder.setCancelable(cancellable)
        builder.show()
    }
}