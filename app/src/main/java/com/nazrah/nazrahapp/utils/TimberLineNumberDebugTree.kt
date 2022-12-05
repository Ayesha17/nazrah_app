package com.nazrah.nazrahapp.utils

import timber.log.Timber

class TimberLineNumberDebugTree  (private val tag: String) : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement) =
            "$tag: (${element.fileName}:${element.lineNumber}) #${element.methodName} "
    }
