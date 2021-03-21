package com.data.repositories.remote

import androidx.annotation.VisibleForTesting
import com.data.repositories.BaseRepository
import com.data.webservices.IService
import com.domain.datasources.remote.IRestDataSource

class RestDataRepositary(
    private val service: IService
) : BaseRepository(), IRestDataSource {

    companion object {
        private var INSTANCE: RestDataRepositary? = null

        @JvmStatic
        fun getInstance(
            service: IService
        ): RestDataRepositary {
            if (INSTANCE == null) {
                synchronized(RestDataRepositary::javaClass) {
                    INSTANCE =
                        RestDataRepositary(service)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
