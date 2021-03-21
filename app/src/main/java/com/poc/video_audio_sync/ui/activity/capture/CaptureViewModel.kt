package com.poc.video_audio_sync.ui.activity.capture

import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.lifecycle.MutableLiveData
import com.device.utility.getExtAppVideoDir
import com.device.utility.getIntAppVideoCacheDir
import com.poc.video_audio_sync.AppController
import com.poc.video_audio_sync.Injection
import com.support.baseApp.mvvm.MBaseViewModel
import com.support.rxJava.Scheduler.io
import com.support.rxJava.Scheduler.ui
import com.support.utills.file.FileUtility.*
import com.support.utills.file.MIME_TYPE_VIDEO
import org.jetbrains.anko.toast
import java.io.*


class CaptureViewModel : MBaseViewModel(AppController.instance) {
    private val TAG: String = CaptureViewModel::class.java.simpleName

    var fileExportedLiveData: MutableLiveData<String> = MutableLiveData()

    override fun subscribe() {
        //no-op
    }

    fun exportVideo(videoFile: File, audioFile: File) {
        val combinedVideoFile =
            File(getContext().getIntAppVideoCacheDir(), "combined_video.mp4").also {
                it.delete()
            }

        showLoader()
        Injection.provideFFMpegRepository().getVideoAudioMux(
            videoFile.absolutePath,
            audioFile.absolutePath,
            combinedVideoFile.absolutePath
        )
            .subscribeOn(io())
            .observeOn(ui()).subscribe(
                { value ->
                    getContext().toast("onNext $value")
                },
                { error ->
                    getContext().toast("onError ${error.localizedMessage}")
                    hideLoader()
                },
                {
                    moveFile(combinedVideoFile)
                    getContext().toast("onCompleted")
                    hideLoader()
                })
    }

    private fun moveFile(combinedVideoFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveMedia(
                getContext(),
                combinedVideoFile,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                combinedVideoFile.getName(),
                MIME_TYPE_VIDEO,
                Environment.DIRECTORY_MOVIES
            )
        } else {
            fileMoving(combinedVideoFile, getContext().getExtAppVideoDir());
        }
        notifyMediaGallery(getContext().getExtAppVideoDir().absolutePath, getContext());
        fileExportedLiveData.value = getContext().getExtAppVideoDir().absolutePath

    }

}