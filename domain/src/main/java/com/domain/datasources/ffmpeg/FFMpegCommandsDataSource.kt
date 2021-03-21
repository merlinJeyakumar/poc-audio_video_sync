package com.domain.datasources.ffmpeg

interface FFMpegCommandsDataSource {
    fun getVideoAudioMuxCommand(videoPath:String, musicPath:String,outputPath:String):String
}