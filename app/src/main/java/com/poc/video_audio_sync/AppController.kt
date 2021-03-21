package com.poc.video_audio_sync

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.poc.video_audio_sync.ui.activity.CrashActivity
import org.jetbrains.anko.runOnUiThread

class AppController : Application(), LifecycleObserver {

    private val TAG = javaClass.simpleName

    companion object {
        lateinit var instance: AppController
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}