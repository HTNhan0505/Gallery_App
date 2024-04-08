package com.example.gallery_app.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_app.Data.Image
import com.example.gallery_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListImageAdapter(private var images:List<Image>, context: Context) :
    RecyclerView.Adapter<ListImageAdapter.ListImageViewHolder>() {

    class ListImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageHome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_image_item, parent, false)
        return ListImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ListImageViewHolder, position: Int) {
        val image = images[position]

        processImages(holder,image.src)
    }
    fun refreshData(newImages: MutableList<Image>) {
        images = newImages
        notifyDataSetChanged()
    }

    suspend fun readBitmapAndScale(path: String): Bitmap {
        val options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = 4
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(path, options)
    }
    private suspend fun getImageBitmap(imageUrl: String): Bitmap? {
        var bitmap: Bitmap? = ImageCache.getBitmapFromCache(imageUrl)
        if (bitmap == null) {
            bitmap = readBitmapAndScale(imageUrl)

            ImageCache.addBitmapToCache(imageUrl, bitmap)
        }
        return bitmap
    }
    private fun processImages(holder: ListImageViewHolder, imagePath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = getImageBitmap(imagePath)

            withContext(Dispatchers.Main) {
                holder.imageView.setImageBitmap(bitmap)
            }

        }


    }
}