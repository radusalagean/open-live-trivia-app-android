package com.busytrack.openlivetriviainterface.extension

import com.google.gson.Gson
import org.json.JSONObject

/**
 * Convert a [Gson] instance to a [JSONObject] instance
 */
fun Gson.toJsonObject(src: Any): JSONObject {
    return JSONObject(toJson(src))
}