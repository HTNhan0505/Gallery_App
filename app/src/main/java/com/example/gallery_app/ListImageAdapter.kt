package com.example.gallery_app

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ListImageAdapter(private var images:List<Image>,context: Context) :
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
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(image.src))
    }
    fun refreshData(newImages: MutableList<Image>) {
        images = newImages
        notifyDataSetChanged()
    }


}