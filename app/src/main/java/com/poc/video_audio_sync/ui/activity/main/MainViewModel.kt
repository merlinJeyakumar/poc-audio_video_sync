package com.poc.video_audio_sync.ui.activity.main

import com.device.utility.getIntAppAudioCacheDir
import com.poc.video_audio_sync.AppController
import com.support.baseApp.mvvm.MBaseViewModel
import com.support.utills.file.FileUtility.copyAssets


class MainViewModel : MBaseViewModel(AppController.instance) {
    private val TAG: String = MainViewModel::class.java.simpleName

    override fun subscribe() {
        //no-op
    }

    fun cacheSounds() {
        copyAssets(getContext(),"songs",getContext().getIntAppAudioCacheDir().absolutePath)
    }

}