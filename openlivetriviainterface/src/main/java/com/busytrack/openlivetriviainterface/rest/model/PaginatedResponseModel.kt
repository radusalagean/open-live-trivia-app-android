package com.busytrack.openlivetriviainterface.rest.model

import com.google.gson.annotations.SerializedName

data class PaginatedResponseModel<T>(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("itemsCount") val itemsCount: Int,
    @SerializedName("perPage") val perPage: Int,
    @SerializedName("items") val items: List<T>
)