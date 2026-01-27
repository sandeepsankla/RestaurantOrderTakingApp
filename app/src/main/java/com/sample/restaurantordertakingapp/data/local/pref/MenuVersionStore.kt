package com.sample.restaurantordertakingapp.data.local.pref


import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuVersionStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getLocalVersion(): Int {
        return prefs.getInt(KEY_MENU_VERSION, 0)
    }

    fun saveLocalVersion(version: Int) {
        prefs.edit()
            .putInt(KEY_MENU_VERSION, version)
            .apply()
    }

    companion object {
        private const val PREF_NAME = "menu_pref"
        private const val KEY_MENU_VERSION = "menu_version"
    }
}
