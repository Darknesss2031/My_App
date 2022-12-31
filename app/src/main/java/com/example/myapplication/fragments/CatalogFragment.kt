package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.classes.Tractor
import com.example.myapplication.adapters.TractorListAdapter
import com.example.myapplication.databinding.FragmentCatalogBinding

class CatalogFragment(val trList: ArrayList<Tractor>) : Fragment() {

    lateinit var binding: FragmentCatalogBinding
    private val rvAdapter = TractorListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rvAdapter.setTractorList(trList)
        binding = FragmentCatalogBinding.inflate(layoutInflater)
        init()
        return binding.root
    }


    private fun init() {
        binding.apply {
            tractorRV.adapter = rvAdapter
            tractorRV.layoutManager = LinearLayoutManager(this@CatalogFragment.context)
        }
        binding.butOpenFilter.setOnClickListener {
            binding.apply {
                tractorRV.visibility = View.GONE
                filterFrame.visibility = View.VISIBLE
                butOpenFilter.visibility = View.GONE
                butCloseFilter.visibility = View.VISIBLE
            }
        }
        binding.butCloseFilter.setOnClickListener {
            binding.apply {
                filterFrame.visibility = View.GONE
                tractorRV.visibility = View.VISIBLE
                butCloseFilter.visibility = View.GONE
                butOpenFilter.visibility = View.VISIBLE
            }
        }
    }


}