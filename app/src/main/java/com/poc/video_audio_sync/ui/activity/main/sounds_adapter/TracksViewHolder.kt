package com.poc.video_audio_sync.ui.activity.main.sounds_adapter

import com.poc.video_audio_sync.databinding.ItemMusicListBinding
import com.poc.video_audio_sync.model.FileModel
import com.support.widgets.BaseViewHolder
import java.io.File

class TracksViewHolder(
    private val binding: ItemMusicListBinding,
    private val itemListener: MediaAdapter.ItemListener?
) :
    BaseViewHolder<File>(binding.root) {
    private val TAG: String = "JK"

    override fun bind(position: Int, file: File) {
        binding.trackNameAppCompatTextView.text = file.name
        binding.root.setOnClickListener {
            itemListener?.onItemSelected(position,file)
        }
    }
}