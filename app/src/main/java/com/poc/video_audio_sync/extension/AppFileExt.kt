package com.poc.video_audio_sync.extension

import android.content.Context
import android.os.Environment
import com.poc.video_audio_sync.AppController
import com.poc.video_audio_sync.R
import com.poc.video_audio_sync.model.FileModel
import java.io.File


var MEDIA_TYPE: Int = 0
var IMAGE_TYPE: Int = 1
var VIDEO_TYPE: Int = 2

fun getMediaDirectory(): File? {
    File("${Environment.getExternalStorageDirectory()?.absolutePath}${File.separator}WhatsApp${File.separator}Media${File.separator}.statuses").let {
        return if (it.exists()) {
            it
        } else {
            null
        }
    }
}

fun Context.getStoreDirectory(): File? {
    val saveDirectory = File(
        "${Environment.getExternalStorageDirectory()?.absolutePath}${File.separator}${
            this.getString(
                R.string.app_name
            )
        }"
    )
    saveDirectory.mkdirs()
    if (!saveDirectory.exists()) {
        return null
    }
    return saveDirectory
}

fun Array<File>.getMediaMap(): MutableMap<Int, List<FileModel>> {
    val mediaMap = mutableMapOf<Int, List<FileModel>>()
    val imageList = mutableListOf<FileModel>()
    val videoList = mutableListOf<FileModel>()
    val mediaList = mutableListOf<FileModel>()

    this.forEach {
        if (it.extension.contains("jpg")) {
            val fileModel = FileModel(
                it.absolutePath,
                it.name,
                type = IMAGE_TYPE
            )
            imageList.add(fileModel)
            mediaList.add(fileModel)
        }
        if (it.extension.contains("mp4") || it.extension.contains("3gp")) {
            val fileModel = FileModel(
                it.absolutePath,
                it.name,
                type = VIDEO_TYPE
            )
            videoList.add(fileModel)
            mediaList.add(fileModel)
        }
    }

    mediaMap[IMAGE_TYPE] = imageList
    mediaMap[VIDEO_TYPE] = videoList
    mediaMap[MEDIA_TYPE] = mediaList
    return mediaMap
}

public val File.extension: String
    get() = name.substringAfterLast('.', "")