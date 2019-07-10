package com.itsmeantonio.to_do.Controller

import android.app.Application
import com.itsmeantonio.to_do.Utilities.SharedPrefs

class App : Application() {
    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}