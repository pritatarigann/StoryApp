package com.dicoding.com.storyapp.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast

fun View.showLoading(isLoading: Boolean) {
    this.visibility = if (isLoading) View.VISIBLE else View.GONE
}

fun Context.showToast(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}