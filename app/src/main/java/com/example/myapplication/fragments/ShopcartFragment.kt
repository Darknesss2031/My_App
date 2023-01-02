package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.GlobalList
import com.example.myapplication.adapters.ShopcartListAdapter
import com.example.myapplication.databinding.FragmentShopcartBinding

class ShopcartFragment : Fragment() {
    lateinit var binding: FragmentShopcartBinding
    private val listAdapter = ShopcartListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopcartBinding.inflate(layoutInflater)
        listAdapter.setTractorList(GlobalList.tractorList)
        init()
        return binding.root
    }
    private fun init() {
        binding.apply {
            shopcartListView.adapter = listAdapter
            shopcartListView.layoutManager = LinearLayoutManager(this@ShopcartFragment.context)
        }
    }

}