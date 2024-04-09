package com.example.gallery_app.Controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.gallery_app.Adapter.ImageCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HandlerBitmap {
    suspend fun getImageBitmap(imageUrl: ArrayList<String>): MutableList<Bitmap> =
        withContext(Dispatchers.IO) {
            val bitmapList = mutableListOf<Bitmap>()
            for (item in imageUrl) {
                var bitmap: Bitmap? = ImageCache.getBitmapFromCache(item)
                if (bitmap == null) {
                    bitmap = readBitmapAndScale(item)
                    bitmapList.add(bitmap)

                    ImageCache.addBitmapToCache(item, bitmap)
                } else {
                    bitmapList.add(bitmap)
                }
            }
            return@withContext bitmapList
        }

    suspend fun readBitmapAndScale(path: String): Bitmap {
        val options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = 4
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(path, options)
    }
}