package com.busytrack.openlivetrivia.generic.recyclerview

interface DynamicLoadAdapter<T> {
    /**
     * Shows a loading placeholder while more items are being loaded
     */
    fun showLoadingPlaceholder()

    /**
     * Hides a previously shown loading placeholder
     */
    fun hideLoadingPlaceholder()

    /**
     * Sets the entire list of items of type [T]
     */
    fun setList(list: List<T>)

    /**
     * Clears the list of items
     */
    fun clearList()
}