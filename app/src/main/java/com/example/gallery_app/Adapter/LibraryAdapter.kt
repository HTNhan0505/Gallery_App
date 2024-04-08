package com.example.gallery_app.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gallery_app.Controller.DataHelper
import com.example.gallery_app.Data.Image
import com.example.gallery_app.R

class LibraryAdapter(private var imageList:List<String>, private val showBtn: (Boolean) -> Unit) :
    RecyclerView.Adapter<LibraryAdapter.LibraryHolder>() {
    private var isEnable = true
    private lateinit var db: DataHelper
    private var itemSelectList: MutableList<String> = mutableListOf()
    private var checkedItems: MutableList<Boolean> = mutableListOf()

    class LibraryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageShow)
        val selected: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_item, parent, false)
        setCheckSizeItem(imageList.size)
        db = DataHelper(parent.context)
        showBtn(false)
        return LibraryHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: LibraryHolder, position: Int) {
        val image = imageList[position]
        val isChecked = checkedItems[position]

        Glide.with(holder.itemView)
            .load(image)
            .apply(RequestOptions.centerCropTransform())
            .into(holder.imageView)

        holder.selected.setOnClickListener {
            if (itemSelectList.contains(image)) {
                itemSelectList.remove(image)
                holder.selected.isChecked = isChecked
                checkedItems[position] = isChecked
                if (itemSelectList.isEmpty()) {
                    showBtn(false)

                }
            } else if (isEnable) {
                selectItem(holder, image, position)
            }
        }

        holder.imageView.setOnClickListener {
            if (itemSelectList.contains(image)) {
                itemSelectList.remove(image)
                holder.selected.isChecked = isChecked
                checkedItems[position] = isChecked
                if (itemSelectList.isEmpty()) {
                    showBtn(false)

                }
            } else if (isEnable) {
                selectItem(holder, image, position)
            }
        }

    }

//    fun setImageList(images: List<String>) {
//        imageList = images
//        setCheckSizeItem(imageList.size)
//    }

    private fun setCheckSizeItem(size: Int) {
        checkedItems = MutableList(size) { false }
    }

    private fun selectItem(holder: LibraryHolder, item: String, position: Int) {
        isEnable = true
        itemSelectList += item
        checkedItems[position] = true
        holder.selected.isChecked = true
        showBtn(true)
    }


    fun saveImageInDB() {
        itemSelectList.forEachIndexed { index, s ->
            var image = Image(index,s)
            db.addImage(image)
        }
        showBtn(false)
        notifyDataSetChanged()

    }



}