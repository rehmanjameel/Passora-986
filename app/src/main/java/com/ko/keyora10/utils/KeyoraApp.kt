package com.ko.keyora10.utils

import android.app.Application

class KeyoraApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppPreferences.init(this)
    }
}