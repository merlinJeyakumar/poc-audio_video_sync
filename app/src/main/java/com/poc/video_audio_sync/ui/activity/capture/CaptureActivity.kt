package com.poc.video_audio_sync.ui.activity.capture

import android.content.DialogInterface
import android.graphics.PointF
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.device.utility.getIntAppAudioCacheDir
import com.device.utility.getIntAppVideoCacheDir
import com.otaliastudios.cameraview.*
import com.poc.video_audio_sync.R
import com.poc.video_audio_sync.databinding.LayoutMainBinding
import com.poc.video_audio_sync.extension.*
import com.support.baseApp.mvvm.MBaseActivity
import com.support.baseApp.mvvm.permission.MEasyPermissions
import com.support.dialog.getConfirmationDialog
import com.support.dialog.getInformationDialog
import kotlinx.android.synthetic.main.layout_capture.*
import kotlinx.android.synthetic.main.layout_main.*
import java.io.File


class CaptureActivity : MBaseActivity<LayoutMainBinding, CaptureViewModel>() {
    private lateinit var audioName: String
    private lateinit var audioFile: File
    private lateinit var videoFile: File
    private var isMediaPlayerPrepared: Boolean = false
    private lateinit var mediaPlayer: MediaPlayer
    private var captureTime: Long = 0
    private val TAG: String = "MainActivity"

    override fun initializeViewModel(): CaptureViewModel {
        return obtainViewModel(CaptureViewModel::class.java)
    }

    private fun initPreview() {
        setRecordingView(false)
    }

    private fun initObserver() {
        viewModel.fileExportedLiveData.observe(this@CaptureActivity, Observer {
            getInformationDialog(message = "file saved into $it")
        })
    }

    private fun initPermission() {
        if (MEasyPermissions.hasPermissions(
                this@CaptureActivity,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO
            )
        ) {
            openCamera()
        } else {
            MEasyPermissions.requestPermissions(
                this@CaptureActivity,
                REQ_STORAGE_PERMISSION,
                object : MEasyPermissions.PermissionCallbacks {
                    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
                        openCamera()
                    }

                    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
                        getConfirmationDialog(
                            message = "Require Permission",
                            isCancellable = false,
                            negativeText = "Close",
                            positiveText = "Enable",
                            dialogInterface = DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        initPermission()
                                    }
                                    else -> {
                                        finishAffinity()
                                    }
                                }
                            }
                        )
                    }

                    override fun onPermissionsPermanentlyDeclined(
                        requestCode: Int,
                        perms: List<String>
                    ) {
                        getConfirmationDialog(
                            message = "Require Permission",
                            isCancellable = false,
                            negativeText = "Close",
                            positiveText = "Enable",
                            dialogInterface = DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        MEasyPermissions.startSetting()
                                    }
                                    else -> {
                                        finishAffinity()
                                    }
                                }
                            }
                        )
                    }

                },

                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun initListener() {
        lottie_layer_name.setOnClickListener {
            if (cameraView.isTakingVideo) {
                cameraView.stopVideo()
            } else {
                videoFile = File(getIntAppVideoCacheDir(), "videoFile.mp4").also {
                    it.delete()
                }
                cameraView.takeVideoSnapshot(videoFile)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        MEasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        if (MEasyPermissions.hasPermissions(
                this@CaptureActivity,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO
            )
        ) {
            openCamera()
        }
    }

    private fun openCamera() {
        cameraView.open()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        cameraView.close()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getBaseLayoutId(): Int {
        return R.layout.layout_capture
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        initData()
        initObserver()
        initPermission()
        initListener()
        initPreview()
        initCamera()
        initPlayer()
    }

    private fun initData() {
        audioName = intent.getStringExtra(INTENT_AUDIO_NAME)
    }

    private fun initPlayer() {
        audioFile = File(getIntAppAudioCacheDir(), audioName)
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this@CaptureActivity, Uri.fromFile(audioFile))
        mediaPlayer.prepareAsync()
        isMediaPlayerPrepared = false
        mediaPlayer.setOnPreparedListener {
            isMediaPlayerPrepared = true
        }
        mediaPlayer.setOnCompletionListener {
            setRecordingView(false)
        }
    }

    private fun initCamera() {
        CameraLogger.setLogLevel(CameraLogger.LEVEL_WARNING)
        cameraView.addCameraListener(Listener())
        cameraView.setLifecycleOwner(this)
    }

    private inner class Listener : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
            //to-do
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            getInformationDialog(message = "Got CameraException #" + exception.reason)
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            if (cameraView.isTakingVideo) {
                getInformationDialog(message = "Captured while taking video. Size=" + result.size)
                return
            }

            // This can happen if picture was taken with a gesture.
            val callbackTime = System.currentTimeMillis()
            if (captureTime == 0L) captureTime = callbackTime - 300
            captureTime = 0
        }

        override fun onVideoTaken(result: VideoResult) {
            super.onVideoTaken(result)
        }

        override fun onVideoRecordingStart() {
            super.onVideoRecordingStart()
            mediaPlayer.start()
            setRecordingView(true)
            Log.i(TAG, "onVideoRecordingStart!")
        }

        override fun onVideoRecordingEnd() {
            super.onVideoRecordingEnd()
            Log.i(TAG, "onVideoRecordingEnd!")
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            setRecordingView(false)
            viewModel.exportVideo(videoFile, audioFile)
        }

        override fun onExposureCorrectionChanged(
            newValue: Float,
            bounds: FloatArray,
            fingers: Array<PointF>?
        ) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers)
            getInformationDialog(message = "Exposure correction:$newValue")
        }

        override fun onZoomChanged(newValue: Float, bounds: FloatArray, fingers: Array<PointF>?) {
            super.onZoomChanged(newValue, bounds, fingers)
            getInformationDialog(message = "Zoom:$newValue")
        }
    }

    private fun setRecordingView(isRecording: Boolean) {
        if (isRecording) {
            lottie_layer_name.setAnimation("recording_stop_button.json")
            lottie_layer_name.playAnimation()
            lottie_layer_name.repeatCount = 0;
        } else {
            lottie_layer_name.clearAnimation()
            lottie_layer_name.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_fiber_manual_record_24))
        }
    }

    companion object {
        val INTENT_AUDIO_NAME = "INTENT_AUDIO_NAME"
    }
}
