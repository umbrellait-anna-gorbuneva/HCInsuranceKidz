package com.umbrellait.hcinsurancekidz

import android.app.Application
import timber.log.Timber

class HCIKidzApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}