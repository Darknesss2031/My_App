package com.example.myapplication.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentGeninfoBinding
import com.squareup.picasso.Picasso

class GeninfoFragment : Fragment() {

    lateinit var binding: FragmentGeninfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeninfoBinding.inflate(layoutInflater)
        Picasso.with(this.context).load("https://www.tehnokor.ru/upload/iblock/733/733e28d11eaaad58949fd93f885ee547.jpg").into(binding.imageView)
        return binding.root
    }

}