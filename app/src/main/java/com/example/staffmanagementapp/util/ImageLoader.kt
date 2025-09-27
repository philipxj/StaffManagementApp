package com.example.staffmanagementapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

/**
 * A simple image loader that uses coroutines for asynchronous loading and includes memory caching.
 */
object ImageLoader {
    private val memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    private fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap)
        }
    }

    private fun getBitmapFromMemCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }

    fun load(imageView: ImageView, url: String) {
        val cachedBitmap = getBitmapFromMemCache(url)
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap)
            return
        }

        // Using GlobalScope is a simplified approach, CoroutineScope should be injected in real large projects
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                if (bitmap != null) {
                    addBitmapToMemoryCache(url, bitmap)
                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}