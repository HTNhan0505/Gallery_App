package com.example.gallery_app


import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class Library : Fragment() {
    private lateinit var binding: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var images: ArrayList<String>
    private lateinit var imageAdapter: MainAdapter
    private val PERMISSION_CODE = 101


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_library, container, false)
        binding = root

        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        } else {
            loadImage()
            showSaveBtn(false)
        }


        return root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImagesFromGallery()
            } else {
                return
            }
        }
    }

    fun loadImage() {
        val imagePaths = loadImagesFromGallery()
        recyclerView = binding.findViewById(R.id.listImage)
        recyclerView.setHasFixedSize(true)
        imageAdapter = MainAdapter(){ show -> showSaveBtn(show) }
        imageAdapter.setImageList(imagePaths)

        this.activity?.baseContext?.let {
            images = loadImagesFromGallery()
            recyclerView.layoutManager = GridLayoutManager(it, 3)
        }

        recyclerView.adapter = imageAdapter
    }


    private fun loadImagesFromGallery(): ArrayList<String> {
        val imagePaths = ArrayList<String>()

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(columnIndex)
                imagePaths.add(imagePath)
            }
        }

        return imagePaths
    }



    fun showSaveBtn(show: Boolean) {
        if (show) {
            binding.findViewById<AppCompatButton>(R.id.saveImg).visibility = View.VISIBLE
        } else {
            binding.findViewById<AppCompatButton>(R.id.saveImg).visibility = View.INVISIBLE

        }
    }

    private fun savDB() {
        imageAdapter.saveImageInDB()
        showSaveBtn(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.findViewById<AppCompatButton>(R.id.back_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_library2_to_listImageFragment)
        }
        binding.findViewById<AppCompatButton>(R.id.saveImg).setOnClickListener {
            savDB()
        }

    }


}
