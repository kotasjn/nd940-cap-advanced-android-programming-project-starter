package com.example.android.politicalpreparedness.network.models

data class Address (
        var line1: String? = null,
        var line2: String? = null,
        var city: String? = null,
        var state: String? = null,
        var zip: String? = null
) {
    fun toFormattedString(): String {
        return line1.plus("\n").plus(line2).plus("\n").plus("$city, $state $zip")
    }

    fun isEmpty(): Boolean {
        return line1.isNullOrEmpty() || city.isNullOrEmpty() || state.isNullOrEmpty() || zip.isNullOrEmpty()
    }
}