package com.example.gallery_app

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gallery_app.databinding.FragmentLibraryBinding
import com.example.gallery_app.databinding.LibraryItemBinding

class MainAdapter(private val showBtn: (Boolean) -> Unit) :
    RecyclerView.Adapter<MainAdapter.LibraryHolder>() {
    private var isEnable = false
    private val itemSelectList: MutableList<String> = mutableListOf()

    private var imageList: List<String> = listOf()

    class LibraryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageShow)
        val selected: ImageView = itemView.findViewById(R.id.imageSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_item, parent, false)
        return LibraryHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: LibraryHolder, position: Int) {
        val image = imageList[position]
        holder.selected.visibility = View.GONE
        Glide.with(holder.itemView)
            .load(image)
            .apply(RequestOptions.centerCropTransform())
            .into(holder.imageView)

        holder.imageView.setOnLongClickListener {
            selectItem(holder,image,position)
            true
        }

        holder.imageView.setOnClickListener {
            if(itemSelectList.contains(image)) {
                itemSelectList.remove(image)
                holder.selected.visibility = View.GONE
                if(itemSelectList.isEmpty()) {
                    showBtn(false)
                    isEnable = false
                }
            } else if(isEnable) {
                selectItem(holder,image,position)
            }
        }

    }

    fun setImageList(images: List<String>) {
        imageList = images
    }

    private fun selectItem(holder: LibraryHolder,item:String ,position: Int) {
        isEnable = true
        itemSelectList+=item
        holder.selected.visibility = View.VISIBLE
        showBtn(true)
    }


    fun saveImageInDB() {

        println("List : $itemSelectList")
    }


}