package com.data.webservices

import com.google.gson.Gson
import com.google.gson.GsonBuilder


class GsonUtils private constructor() {
    public var gson: Gson

    init {
        val gsonBuilder = GsonBuilder().setLenient()
        registerTypeAdapters(gsonBuilder)
        gson = gsonBuilder.create()
    }

    private fun registerTypeAdapters(gsonBuilder: GsonBuilder) {
        addSerializers(gsonBuilder)
//        gsonBuilder.registerTypeAdapter(MError::class.java, ErrorDs())
    }

    private fun addSerializers(gsonBuilder: GsonBuilder) {
    }

    companion object {
        private var instance: GsonUtils? = null

        fun getInstance(): GsonUtils {
            if (instance == null) {
                instance =
                    GsonUtils()
            }
            return instance as GsonUtils
        }
    }
}
