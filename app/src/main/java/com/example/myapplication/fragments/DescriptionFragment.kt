package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.Constance
import com.example.myapplication.adapters.VpImageAdapter
import com.example.myapplication.databinding.FragmentDescriptionBinding

class DescriptionFragment(val imURLList: ArrayList<String>, val fullDesc: String?, val videoURL: String? = null) : Fragment() {
    lateinit var binding: FragmentDescriptionBinding

    private var imageFragList = arrayListOf<Fragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(layoutInflater)
        for (el in imURLList) {
            val newFrag = ImageFragment.newInstance(el)
            imageFragList.add(newFrag)
        }
        binding.textDesc.text = fullDesc!!
        val adapter = VpImageAdapter(this@DescriptionFragment, imageFragList)
        binding.imageCont.adapter = adapter
        if (videoURL != null) {
            val displayedString = Constance.videoDesc + videoURL
            binding.textVideoLink.text = displayedString
        }
        return binding.root
    }
}