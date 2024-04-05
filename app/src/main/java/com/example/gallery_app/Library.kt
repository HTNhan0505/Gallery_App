package com.example.gallery_app


import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import com.example.gallery_app.databinding.FragmentLibraryBinding
import com.example.gallery_app.databinding.FragmentListImageBinding
import java.io.File


class Library : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var images: ArrayList<String>
    private lateinit var imageAdapter: MainAdapter
    private val PERMISSION_CODE = 101


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("If : ")
            requestPermission()
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                PERMISSION_CODE
//            )
        } else {
            println("Else : ")
            loadImage()
            showSaveBtn(false)
        }


        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImage()
            } else {
                println("Decline ")
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES,android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        }
    }

    fun loadImage() {
        println("Load ")
        val imagePaths = loadImagesFromGallery()
        imageAdapter = MainAdapter(imagePaths) { show -> showSaveBtn(show) }

        recyclerView = binding.root.findViewById(R.id.listImage)

        recyclerView.setHasFixedSize(true)

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
            binding.root.findViewById<AppCompatButton>(R.id.saveImg).visibility = View.VISIBLE
        } else {
            binding.root.findViewById<AppCompatButton>(R.id.saveImg).visibility = View.INVISIBLE

        }
    }

    private fun savDB() {
        imageAdapter.saveImageInDB()
        showSaveBtn(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.findViewById<AppCompatButton>(R.id.back_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_library2_to_listImageFragment)
        }
        binding.root.findViewById<AppCompatButton>(R.id.saveImg).setOnClickListener {
            savDB()
        }
    }


}
