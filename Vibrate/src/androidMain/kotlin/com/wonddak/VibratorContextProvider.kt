package com.wonddak

import android.content.Context
import java.lang.ref.WeakReference

/**
 * VibratorManager context Provider
 */
object VibratorContextProvider {
    private var contextRef: WeakReference<Context>? = null

    fun init(context: Context) {
        contextRef = WeakReference(context.applicationContext)
    }

    fun getContext(): Context {
        val context = contextRef?.get()
        return context ?: throw IllegalStateException("VibratorContextProvider is not initialized or context has been garbage collected.")
    }
}