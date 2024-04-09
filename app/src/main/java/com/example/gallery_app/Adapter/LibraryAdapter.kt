package com.example.gallery_app.Adapter


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.collection.LruCache
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_app.Controller.DataHelper
import com.example.gallery_app.Data.Image
import com.example.gallery_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL

class LibraryAdapter(
    private var bitmapList: MutableList<Bitmap>,
    private var imageList: List<String>,
    private val showBtn: (Boolean) -> Unit
) :
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
        val bitmapSrc = bitmapList[position]

        val isChecked = checkedItems[position]
        holder.imageView.setImageBitmap(bitmapSrc)

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
            var image = Image(index, s)
            db.addImage(image)
        }
        showBtn(false)
        notifyDataSetChanged()

    }


}