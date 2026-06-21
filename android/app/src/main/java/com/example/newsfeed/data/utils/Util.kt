package com.example.newsfeed.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Util {
     fun formatDate(dateString: String): String {
        return if (dateString.length >= 10) {
            dateString.substring(0, 10) // Lấy "YYYY-MM-DD"
        } else {
            dateString
        }
    }
  //Kiểm tra có kết nối mạng không
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}