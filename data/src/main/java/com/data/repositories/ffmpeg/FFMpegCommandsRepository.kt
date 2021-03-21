package com.data.repositories.ffmpeg

import androidx.annotation.VisibleForTesting
import com.data.repositories.BaseRepository
import com.domain.datasources.ffmpeg.FFMpegCommandsDataSource

class FFMpegCommandsRepository() : BaseRepository(), FFMpegCommandsDataSource {

    companion object {

        private var INSTANCE: FFMpegCommandsRepository? = null

        @JvmStatic
        fun getInstance(): FFMpegCommandsRepository {
            if (INSTANCE == null) {
                synchronized(FFMpegCommandsRepository::javaClass) {
                    INSTANCE =
                        FFMpegCommandsRepository()
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    override fun getVideoAudioMuxCommand(
        videoPath: String,
        musicPath: String,
        outputPath: String
    ): String {
        //return "-i $videoPath -af \"highpass=f=200, lowpass=f=3000\" $outputPath"
        //return "-i $videoPath -i $musicPath -map 0:v -map 0:a -map 1:a -map 0:s -c copy -f $outputPath"
        //return "-i $videoPath -i $musicPath -filter_complex [0:a:0][1:a:0]amix=inputs=2:duration=longest[aout] -c:v copy -map 0:v:0 -map [aout] -c:a aac -b:a 192k $outputPath"
        return "-i \"$videoPath\" -i \"$musicPath\" -vcodec copy -filter_complex amix -map 0:v -map 0:a -map 1:a -shortest -b:a 144k \"$outputPath\""
        return "-i $videoPath -i $musicPath -filter_complex \"[0:a][1:a]amerge=inputs=2[a]\" -map 0:v -map \"[a]\" -c:v copy -ac 2 -shortest $outputPath"
        return "-i $videoPath -i $musicPath -map 0:v -map 1:a -c copy -y $outputPath"
        return "-i $videoPath -i $musicPath -af afade=t=in:st=0:d=3,afade=t=out:st=47:d=4 -c:v copy -c:a aac -shortest $outputPath"
    }
}
