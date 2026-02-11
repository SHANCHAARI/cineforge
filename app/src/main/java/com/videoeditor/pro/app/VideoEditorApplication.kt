package com.videoeditor.pro.app

import android.app.Application
import com.videoeditor.pro.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class VideoEditorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Load native library
        System.loadLibrary("videoeditor_engine")
        
        Timber.d("VideoEditorPro Application started")
        
        // Initialize native engine
        initializeNativeEngine()
    }
    
    private external fun initializeNativeEngine()
}
