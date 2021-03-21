package com.poc.video_audio_sync.ui.activity.main.sounds_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.poc.video_audio_sync.databinding.ItemMusicListBinding
import com.support.widgets.BaseViewHolder
import java.io.File

class MediaAdapter(
    private var fileList: MutableList<File>,
    private var itemListener: ItemListener?
) : RecyclerView.Adapter<BaseViewHolder<File>>() {
    private val selectionList = mutableMapOf<String, Pair<Int, File>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<File> {
        return TracksViewHolder(
            ItemMusicListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemListener
        )

    }

    override fun getItemCount(): Int = fileList.size

    override fun onBindViewHolder(holder: BaseViewHolder<File>, position: Int) {
        holder.bind(
            position,
            fileList[position]
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<File>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
    }

    fun setData(newRating: List<File>) {
        val diffCallback = MediaDiffCallback(fileList, newRating)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        fileList.clear()
        fileList.addAll(newRating)
        selectionList.clear()
        diffResult.dispatchUpdatesTo(this)
    }

    fun setSelection(mediaId: String, forced: Boolean = false) {
        val mediaPosition: Int = getMediaPositionInList(mediaId)
        if (!forced && selectionList.contains(mediaId)) {
            selectionList.remove(mediaId)
        } else {
            selectionList[mediaId] = Pair(mediaPosition, fileList[mediaPosition])
        }
        notifyItemChanged(mediaPosition, selectionList.keys)
    }

    fun disableSelectionMode() {
        val mutableList: MutableList<String> = mutableListOf()
        mutableList.addAll(selectionList.keys.toList())
        mutableList.forEach {
            setSelection(it)
        }
    }

    fun setAllSelected() {
        fileList.forEach {
            setSelection(it.name,forced = true)
        }
    }

    fun getSelectedItemCount(): Int {
        return selectionList.size
    }

    fun getMediaPositionInList(fileName: String?): Int {
        for (i in fileList.indices) {
            val campaignUploadModel = fileList.get(i).name
            if (campaignUploadModel == fileName) {
                return i
            }
        }
        return -1
    }

    fun getSelectedItems(): MutableList<File> {
        val mutableList: MutableList<File> = mutableListOf()
        selectionList.forEach {
            mutableList.add(it.value.second)
        }
        return mutableList
    }

    override fun getItemId(position: Int): Long {
        return fileList[position].hashCode().toLong()
    }

    fun isAllSelected(): Boolean {
        return selectionList.size == fileList.size
    }

    fun isSelected(mediaId: String? = null): Boolean {
        mediaId?.let {
            return selectionList.containsKey(it)
        }
        return selectionList.isNotEmpty()
    }

    class MediaDiffCallback(
        private val oldList: List<File>,
        private val newList: List<File>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition].totalSpace == newList[newPosition].totalSpace
        }

        @Nullable
        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return super.getChangePayload(oldPosition, newPosition)
        }
    }

    interface ItemListener {
        fun onItemSelected(position: Int, fileModel: File) {}
    }
}
