package com.busytrack.openlivetrivia.generic.recyclerview

interface DynamicLoadAdapter<T> {
    fun showLoadingPlaceholder()
    fun hideLoadingPlaceholder()
    fun setList(list: List<T>)
    fun clearList()
}