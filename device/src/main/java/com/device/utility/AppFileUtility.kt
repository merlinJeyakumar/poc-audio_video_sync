package com.device.utility

import android.content.Context
import android.os.Environment
import java.io.File

fun Context.getIntAppImageCacheDir(): File {
    return File(cacheDir,"Image").also {
        it.mkdir()
    }
}

fun Context.getIntAppVideoCacheDir(): File {
    return File(cacheDir,"Video").also {
        it.mkdir()
    }
}

fun Context.getIntAppAudioCacheDir(): File {
    return File(cacheDir,"Audio").also {
        it.mkdir()
    }
}

fun Context.getIntAppFilesCacheDir(): File {
    return File(cacheDir,"Files").also {
        it.mkdir()
    }
}

fun Context.getExtAppVideoDir(): File {
    return File(Environment.getExternalStorageDirectory(),Environment.DIRECTORY_MOVIES).also {
        it.mkdir()
    }
}