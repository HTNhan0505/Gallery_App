package com.example.gallery_app.Adapter

import android.graphics.Bitmap
import androidx.collection.LruCache

object ImageCache {
    private const val MAX_CACHE_SIZE = 10 * 1024 * 1024
    private val cache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(MAX_CACHE_SIZE) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount
        }
    }

    fun addBitmapToCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromCache(key) == null) {
            cache.put(key, bitmap)
        }
    }

    fun getBitmapFromCache(key: String): Bitmap? {
        return cache.get(key)
    }
}