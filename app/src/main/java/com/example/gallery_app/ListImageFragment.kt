package com.example.gallery_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_app.databinding.FragmentListImageBinding

class ListImageFragment : Fragment() {
    private lateinit var binding: FragmentListImageBinding
    private lateinit var db: DataHelper
    private lateinit var listImageAdapter: ListImageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListImageBinding.inflate(inflater, container, false)
        loadImage()
        return binding.root
    }


    fun loadImage() {
        db = DataHelper(requireActivity().applicationContext)
        recyclerView = binding.root.findViewById(R.id.list_home_image)
        recyclerView.setHasFixedSize(true)
        listImageAdapter = ListImageAdapter(db.getAllImage(),requireActivity().applicationContext)

        this.activity?.baseContext?.let {
            recyclerView.layoutManager = GridLayoutManager(it, 2)
        }

        recyclerView.adapter = listImageAdapter
    }

    override fun onResume() {
        super.onResume()
        listImageAdapter.refreshData(db.getAllImage())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addImgBtn.setOnClickListener {
            findNavController().navigate(R.id.action_listImageFragment_to_library2)
        }


    }


}