package com.example.gallery_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_app.databinding.FragmentLibraryBinding
import com.example.gallery_app.databinding.LibraryItemBinding

class MainAdapter(
    private val dataItemList: ArrayList<DataItem>,
    private val showBtn: (Boolean) -> Unit
) : RecyclerView.Adapter<MainAdapter.LibraryHolder>() {
    private var isEnable = false
    private val itemSelectList = mutableListOf<Int>()


    class LibraryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.imageShow)
        val selected: ImageView = itemView.findViewById(R.id.imageSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_item, parent, false)
        return LibraryHolder(view)
    }

    override fun getItemCount(): Int {
        return dataItemList.size
    }

    override fun onBindViewHolder(holder: LibraryHolder, position: Int) {
        val image = dataItemList[position]
        holder.imageView.setImageResource(image.image)
        holder.selected.visibility = View.GONE

        holder.imageView.setOnLongClickListener {
            selectItem(holder, image, position)
            true
        }


        holder.imageView.setOnClickListener {
            if(itemSelectList.contains(image.image)) {
                itemSelectList.remove(image.image)
                holder.selected.visibility = View.GONE
                image.selected = false
                if(itemSelectList.isEmpty()) {
                    showBtn(false)
                    isEnable = false
                }
            } else if(isEnable) {
                selectItem(holder,image,position)
            }
        }
    }

    private fun selectItem(holder: MainAdapter.LibraryHolder, item: DataItem, position: Int) {
        isEnable = true
        itemSelectList.add(item.image)
        item.selected = true
        holder.selected.visibility = View.VISIBLE
        showBtn(true)
    }


    fun saveImageInDB() {
        println("List : $itemSelectList")
    }

}