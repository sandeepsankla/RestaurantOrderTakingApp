package com.sample.restaurantordertakingapp.data.local.pref

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class AppMode { RECEPTION, KITCHEN }

/**
 * Device-level role (Reception vs Kitchen) + 4-digit PIN, locally saved.
 * Kitchen device sirf Orders dekhta hai; mode switch karne ke liye PIN chahiye.
 */
@Singleton
class RoleStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getMode(): AppMode? =
        prefs.getString(KEY_MODE, null)?.let { runCatching { AppMode.valueOf(it) }.getOrNull() }

    fun getPin(): String? = prefs.getString(KEY_PIN, null)

    fun isPinSet(): Boolean = getPin() != null

    /** First-time setup: role + PIN dono save. */
    fun setup(mode: AppMode, pin: String) {
        prefs.edit().putString(KEY_MODE, mode.name).putString(KEY_PIN, pin).apply()
    }

    /** Sirf mode change (PIN wahi rehta hai). */
    fun setMode(mode: AppMode) {
        prefs.edit().putString(KEY_MODE, mode.name).apply()
    }

    companion object {
        private const val PREF_NAME = "role_pref"
        private const val KEY_MODE = "app_mode"
        private const val KEY_PIN = "app_pin"
    }
}
