package com.poc.video_audio_sync

import android.content.Context
import com.data.database.RoomDataSource
import com.data.database.RoomManager
import com.data.repositories.AppSettingsRepository
import com.data.repositories.ffmpeg.FFMpegCommandsRepository
import com.data.repositories.ffmpeg.FFMpegRepository
import com.data.repositories.local.LocalDataRepositary
import com.data.repositories.remote.RestDataRepositary
import com.data.webservices.IService
import com.domain.datasources.ffmpeg.FFMpegCommandsDataSource
import com.domain.datasources.local.ILocalDataSource
import com.domain.datasources.remote.IRestDataSource
import com.poc.video_audio_sync.data.RestService
import com.google.gson.Gson

object Injection {

    fun provideAppDataSource(): AppSettingsRepository {
        return AppSettingsRepository.getInstance(provideContext(),Gson())
    }

    fun provideRestDataSource(): IRestDataSource {
        return RestDataRepositary.getInstance(provideService())
    }

    fun provideLocalDataSource(): ILocalDataSource {
        return LocalDataRepositary.getInstance(provideRoomDataSource())
    }

    fun provideService(): IService {
        return RestService.getInstance().getIService()
    }

    fun provideRoomDataSource(): RoomDataSource {
        return RoomManager.getInstance(provideContext())
    }

    fun provideContext(): Context {
        return AppController.instance
    }

    fun provideFFMpegRepository(): FFMpegRepository {
        return FFMpegRepository.getInstance(provideFFMpegCommandsRepository())
    }

    fun provideFFMpegCommandsRepository(): FFMpegCommandsDataSource {
        return FFMpegCommandsRepository.getInstance()
    }
}