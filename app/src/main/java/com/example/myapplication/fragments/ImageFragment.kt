package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentImageBinding
import com.squareup.picasso.Picasso

class ImageFragment(private val imageURL: String) : Fragment() {

    companion object {
        fun newInstance(imageURL: String) = ImageFragment(imageURL)
    }

    lateinit var binding: FragmentImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(layoutInflater)
        Picasso.with(this.context).load(imageURL).into(binding.trImageView)
        return binding.root
    }

}