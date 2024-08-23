package com.ntech.theyardhub.core.utils

import android.content.SharedPreferences
import com.google.gson.Gson

class DataStorage(sharedPreferences: SharedPreferences) {
    private var pref: SharedPreferences = sharedPreferences
    private var editor: SharedPreferences.Editor = pref.edit()

    companion object {
        const val KEY_USER_NAME = "KEY_USER_NAME"
        const val KEY_USER_ID = "KEY_USER_ID"
        const val KEY_USER = "KEY_USER"
    }

    var userName: String
        get() {
            return pref.getString(KEY_USER_NAME, "").orEmpty()
        }
        set(value) {
            editor.putString(KEY_USER_NAME, value)
            editor.apply()
        }

    var userId: String
        get() {
            return pref.getString(KEY_USER_ID, "").orEmpty()
        }
        set(value) {
            editor.putString(KEY_USER_ID, value)
            editor.apply()
        }

    var user: String
        get() {
            return pref.getString(KEY_USER, "").orEmpty()
        }
        set(value) {
            val gson = Gson()
            val json = gson.toJson(user)
            editor.putString(KEY_USER, value)
            editor.apply()
        }
}