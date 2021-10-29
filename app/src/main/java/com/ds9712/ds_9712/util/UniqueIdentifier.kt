package com.ds9712.ds_9712.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class UniqueIdentifier {
    companion object {
        @SuppressLint("HardwareIds")
        fun returnAndroidId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }
}