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
        const val KEY_USER_DOCUMENT_ID = "KEY_DOCUMENT_USER_ID"
        const val KEY_IS_GUEST = "KEY_IS_GUEST"
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

    var userDocumentId: String
        get() {
            return pref.getString(KEY_USER_DOCUMENT_ID, "").orEmpty()
        }
        set(value) {
            editor.putString(KEY_USER_DOCUMENT_ID, value)
            editor.apply()
        }

    var isGuest: Boolean
        get() {
            return pref.getBoolean(KEY_IS_GUEST, true)
        }
        set(value) {
            editor.putBoolean(KEY_IS_GUEST, value)
            editor.apply()
        }
}