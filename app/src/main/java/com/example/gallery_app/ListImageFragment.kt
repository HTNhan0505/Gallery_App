package com.example.gallery_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gallery_app.databinding.FragmentListImageBinding

class ListImageFragment : Fragment() {
    private lateinit var binding: FragmentListImageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListImageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addImgBtn.setOnClickListener {

            findNavController().navigate(R.id.action_listImageFragment_to_library2)
        }

    }


}