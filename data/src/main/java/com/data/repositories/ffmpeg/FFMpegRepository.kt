package com.data.repositories.ffmpeg

import androidx.annotation.VisibleForTesting
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.data.repositories.BaseRepository
import com.domain.datasources.ffmpeg.FFMpegCommandsDataSource
import com.domain.datasources.ffmpeg.FFMpegDataSource
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class FFMpegRepository(
    private val ffMpegCommandsDataSource: FFMpegCommandsDataSource
) : BaseRepository(), FFMpegDataSource {

    companion object {

        private var INSTANCE: FFMpegRepository? = null

        @JvmStatic
        fun getInstance(
            ffMpegCommandsDataSource: FFMpegCommandsDataSource
        ): FFMpegRepository {
            if (INSTANCE == null) {
                synchronized(FFMpegRepository::javaClass) {
                    INSTANCE =
                        FFMpegRepository(ffMpegCommandsDataSource)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    fun getVideoAudioMux(
        videoPath: String,
        musicPath: String,
        outputPath:String
    ): @NonNull Observable<Pair<Long, Int>> {
        return getFFMpegExecutor(ffMpegCommandsDataSource.getVideoAudioMuxCommand(videoPath, musicPath,outputPath))
    }

    private fun getFFMpegExecutor(command:String): @NonNull Observable<Pair<Long, Int>> {
        return Observable.create<Pair<Long,Int>> { emitter->
            val executionId = FFmpeg.executeAsync(command) { executionId, returnCode ->
                when (returnCode) {
                    RETURN_CODE_SUCCESS -> {
                        emitter.onComplete()
                    }
                    RETURN_CODE_CANCEL -> {
                        emitter.onError(Exception("Async command execution cancelled by user."))
                    }
                    else -> {
                        emitter.onError(Exception("Async command execution failed with returnCode=$returnCode."))
                    }
                }
            }

            emitter.onNext(Pair(executionId,0))
        }
    }
}
