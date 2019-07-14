package com.busytrack.openlivetriviainterface.extension

import com.google.gson.Gson
import org.json.JSONObject

fun Gson.toJsonObject(src: Any): JSONObject {
    return JSONObject(toJson(src))
}