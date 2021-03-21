package com.poc.video_audio_sync.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.poc.video_audio_sync.R
import kotlinx.android.synthetic.main.activity_crash.*

class CrashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)

        initView()
    }

    private fun initView() {
        //textView.text = intent?.getStringExtra(CRASH_ISSUE)
    }

    companion object {
        @JvmField
        var CRASH_ISSUE: String = "CRASH_ISSUE"
    }
}
