package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.GlobalList
import com.example.myapplication.adapters.ShopcartEquipmentListAdapter
import com.example.myapplication.adapters.ShopcartListAdapter
import com.example.myapplication.databinding.FragmentShopcartViewBinding

class ShopcartViewFragment : Fragment() {
    lateinit var binding: FragmentShopcartViewBinding
    private val trListAdapter = ShopcartListAdapter()
    private val eqListAdapter = ShopcartEquipmentListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopcartViewBinding.inflate(layoutInflater)
        trListAdapter.setTractorList(GlobalList.tractorList)
        eqListAdapter.setTractorList(GlobalList.extraList)
        init()
        return binding.root
    }
    private fun init() {
        binding.apply {
            shopcartListView.adapter = trListAdapter
            shopcartEqListView.adapter = eqListAdapter
            shopcartListView.layoutManager = LinearLayoutManager(this@ShopcartViewFragment.context)
            shopcartEqListView.layoutManager = LinearLayoutManager(this@ShopcartViewFragment.context)
        }
    }

}