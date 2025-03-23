package com.busytrack.openlivetriviainterface.rest

import java.io.File

object NetworkTestUtil {

    /**
     * Helper function which will load JSON
     *
     * @return json : JSON from file at given path
     */
    fun getJson(jsonFileName : String) : String {
        // Load the JSON response
        val uri = this.javaClass.classLoader!!.getResource("json/${jsonFileName}.json")
        val file = File(uri.path)
        return String(file.readBytes())
    }
}