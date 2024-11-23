package com.ntech.theyardhub.di

import android.content.Context
import android.content.SharedPreferences
import com.ntech.theyardhub.core.utils.DataStorage

class SharedPreference {
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("key_the_yard_hub", Context.MODE_PRIVATE)
    }

    fun providePreferenceManager(sharedPreferences: SharedPreferences): DataStorage {
        return DataStorage(sharedPreferences)
    }
}