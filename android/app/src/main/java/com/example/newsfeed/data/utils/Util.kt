package com.example.newsfeed.data.utils

object Util {
     fun formatDate(dateString: String): String {
        return if (dateString.length >= 10) {
            dateString.substring(0, 10) // Lấy "YYYY-MM-DD"
        } else {
            dateString
        }
    }

}