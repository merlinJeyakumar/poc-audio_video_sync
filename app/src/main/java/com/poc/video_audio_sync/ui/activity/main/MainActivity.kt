package com.poc.video_audio_sync.ui.activity.main

import android.content.Intent
import android.os.Bundle
import com.device.utility.getIntAppAudioCacheDir
import com.poc.video_audio_sync.R
import com.poc.video_audio_sync.databinding.LayoutMainBinding
import com.poc.video_audio_sync.extension.obtainViewModel
import com.poc.video_audio_sync.ui.activity.capture.CaptureActivity
import com.poc.video_audio_sync.ui.activity.main.sounds_adapter.MediaAdapter
import com.support.baseApp.mvvm.MActionBarActivity
import kotlinx.android.synthetic.main.layout_main.*
import java.io.File


class MainActivity : MActionBarActivity<LayoutMainBinding, MainViewModel>() {
    private lateinit var mediaAdapter: MediaAdapter
    private val TAG: String = "MainActivity"

    override fun getLayoutId(): Int {
        return R.layout.layout_main
    }

    override fun getHeaderTitle(): String {
        return resources.getString(R.string.app_name)
    }

    override fun isSupportBackOption(): Boolean {
        return true
    }

    override fun initializeViewModel(): MainViewModel {
        return obtainViewModel(MainViewModel::class.java)
    }

    override fun setUpUI(savedInstanceState: Bundle?) {
        initSounds()
        initObserver()
        initListener()
        initAdapter()
        initPreview()
    }

    private fun initSounds() {
        viewModel.cacheSounds()
    }

    private fun initAdapter() {
        mediaAdapter = MediaAdapter(
            getIntAppAudioCacheDir()?.listFiles().toMutableList(),
            object : MediaAdapter.ItemListener {
                override fun onItemSelected(position: Int, fileModel: File) {
                    startActivity(Intent(
                        this@MainActivity,
                        CaptureActivity::class.java
                    ).putExtra(CaptureActivity.INTENT_AUDIO_NAME,fileModel.name))
                }
            }
        )
        musicListRecyclerView.adapter = mediaAdapter
    }

    private fun initPreview() {
        //no-op
    }

    private fun initObserver() {
        /* no-op */
    }

    private fun initListener() {
        /* no-op */
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
