package com.data.webservices

import com.google.gson.JsonElement
import com.google.gson.JsonObject

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by admin on 7/7/17.
 */
fun JsonObject.hasProperty(propertyName: String): Boolean {
    return this.has(propertyName) && !this.isJsonNull
}

class JsonUtils private constructor() {

    fun isJSONValid(test: String): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return false
            }
        }

        return true
    }

    companion object {
        private var instance: JsonUtils? = null

        fun getInstance(): JsonUtils {
            if (instance == null) {
                instance =
                    JsonUtils()
            }
            return instance as JsonUtils
        }

        fun hasProperty(jsonObject: JsonObject, property: String): Boolean {
            return jsonObject.has(property) && !jsonObject.get(property).isJsonNull
        }

        fun getResponseJsonObject(json: JsonElement): JsonObject {
            val jsonObject = json.asJsonObject
            return if (hasProperty(jsonObject, "response")) {
                jsonObject.get("response").asJsonObject
            } else jsonObject
        }

        fun getSuccess(jsonObject: JsonObject): Int {
            return if (hasProperty(jsonObject, "status")) {
                jsonObject.get("status").asInt /*== 200*/
            } else 0
        }

        fun getMessage(jsonObject: JsonObject): String {
            return if (hasProperty(jsonObject, "message")) {
                jsonObject.get("message").asString
            } else ""
        }
    }
}
