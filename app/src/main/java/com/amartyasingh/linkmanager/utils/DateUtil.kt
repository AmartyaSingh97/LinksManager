package com.amartyasingh.linkmanager.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    // Function to convert a timestamp to a readable date format
    fun formatTimestampToDate(timestamp: String): String? {
        // Define the original format of the timestamp
        val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        // Parse the timestamp into a Date object
        val date: Date? = originalFormat.parse(timestamp)
        // Define the desired format for the readable date
        val readableFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        // Format the Date object into the desired readable format and return it
        return date?.let { readableFormat.format(it) }
    }

    fun getChartDateRange(first: String, sec: String): String {
        val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val res1: Date? = df1.parse(first)
        val res2: Date? = df1.parse(sec)
        val requiredFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        return requiredFormat.format(res1) + " - " + requiredFormat.format(res2)
    }
}
