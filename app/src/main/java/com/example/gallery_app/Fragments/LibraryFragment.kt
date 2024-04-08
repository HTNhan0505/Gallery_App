package com.example.gallery_app.Fragments


import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_app.Adapter.LibraryAdapter
import com.example.gallery_app.R
import com.example.gallery_app.databinding.FragmentLibraryBinding


class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var images: ArrayList<String>
    private lateinit var imageAdapter: LibraryAdapter
    private val PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        checkPermissions()
        return binding.root
    }
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                PERMISSION_CODE
//            )
        } else {
            loadImage()
            showSaveBtn(false)
        }
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
        val imagePaths = loadImagesFromGallery()
        imageAdapter = LibraryAdapter(imagePaths) { show -> showSaveBtn(show) }

        recyclerView = binding.listImage
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
        binding.saveImg.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun savDB() {
        imageAdapter.saveImageInDB()
        showSaveBtn(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToHome.setOnClickListener {
            findNavController().navigate(R.id.action_library2_to_listImageFragment)
        }

        binding.saveImg.setOnClickListener {
            savDB()
        }

    }


}
