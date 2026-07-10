package com.wonddak

import android.content.Context
import androidx.startup.Initializer

class VibratorManagerInitializer : Initializer<VibratorManager> {
    override fun create(context: Context): VibratorManager {
        // Auto-initialize once so Android callers can use the shared API without manual wiring.
        return VibratorManager.initializer(context)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}
