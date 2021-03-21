package com.poc.video_audio_sync.model

import android.os.Parcel
import android.os.Parcelable
import java.io.File

data class FileModel(var file: String, var name: String, var type: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileModel> {
        override fun createFromParcel(parcel: Parcel): FileModel {
            return FileModel(parcel)
        }

        override fun newArray(size: Int): Array<FileModel?> {
            return arrayOfNulls(size)
        }
    }
    fun file(): File {
        return File(file)
    }
}