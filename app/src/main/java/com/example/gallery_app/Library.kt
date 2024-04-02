package com.example.gallery_app

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.AdapterView

import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Library : Fragment() {
    private lateinit var binding: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageList: ArrayList<DataItem>
    private lateinit var imageAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_library, container, false)
        binding = root

        init()
        showSaveBtn(false)

        return root
    }

    private fun init() {
        recyclerView = binding.findViewById(R.id.listImage)
        recyclerView.setHasFixedSize(true)

        this.activity?.baseContext?.let {
            recyclerView.layoutManager = GridLayoutManager(it,3)
            imageList = ArrayList()
            addData()
        }


        imageAdapter = MainAdapter(imageList) {show -> showSaveBtn(show)}
        recyclerView.adapter = imageAdapter
    }

    private fun  addData() {
        imageList.add(DataItem(R.drawable.film,false))
        imageList.add(DataItem(R.drawable.camera,false))
        imageList.add(DataItem(R.drawable.frame_100,false))
        imageList.add(DataItem(R.drawable.frame_30,false))
        imageList.add(DataItem(R.drawable.frame_41,false))
        imageList.add(DataItem(R.drawable.frame_39,false))
        imageList.add(DataItem(R.drawable.frame_44,false))
        imageList.add(DataItem(R.drawable.frame_45,false))
        imageList.add(DataItem(R.drawable.frame_49,false))
        imageList.add(DataItem(R.drawable.frame_62,false))
        imageList.add(DataItem(R.drawable.frame_51,false))
        imageList.add(DataItem(R.drawable.frame_98,false))
        imageList.add(DataItem(R.drawable.frame_99,false))
        imageList.add(DataItem(R.drawable.frame_101,false))
        imageList.add(DataItem(R.drawable.frame_31,false))
        imageList.add(DataItem(R.drawable.frame_52,false))
        imageList.add(DataItem(R.drawable.frame_40,false))
        imageList.add(DataItem(R.drawable.frame_42,false))
        imageList.add(DataItem(R.drawable.frame_30,false))
        imageList.add(DataItem(R.drawable.frame_45,false))
        imageList.add(DataItem(R.drawable.frame_44,false))
        imageList.add(DataItem(R.drawable.frame_99,false))
        imageList.add(DataItem(R.drawable.frame_51,false))

    }

    fun showSaveBtn(show:Boolean) {
        if(show) {
            binding.findViewById<AppCompatButton>(R.id.saveImg).visibility = View.VISIBLE
        } else {
            binding.findViewById<AppCompatButton>(R.id.saveImg).visibility = View.INVISIBLE

        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.findViewById<AppCompatButton>(R.id.back_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_library2_to_listImageFragment)
        }

    }

}
