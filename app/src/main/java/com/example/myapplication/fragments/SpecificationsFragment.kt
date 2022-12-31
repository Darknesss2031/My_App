package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.EquipmentListAdapter
import com.example.myapplication.adapters.SpecificationsListAdapter
import com.example.myapplication.databinding.FragmentSpecificationsBinding

class SpecificationsFragment(var spec: ArrayList<ArrayList<String>>) : Fragment() {

    lateinit var binding: FragmentSpecificationsBinding
    private var adapterList = arrayListOf(
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter(),
        SpecificationsListAdapter()
    )
    var RVlist = ArrayList<RecyclerView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        for (idx in adapterList.indices) {
            adapterList[idx].setSpecificationList(spec[idx], idx)
        }
        binding = FragmentSpecificationsBinding.inflate(layoutInflater)
        RVlist = arrayListOf(
            binding.engRV,
            binding.trmRV,
            binding.moveRV,
            binding.steerRV,
            binding.hydroRV,
            binding.agrRV,
            binding.electroRV,
            binding.sizeRV,
            binding.fuelRV,
            binding.kitRV
        )
        Log.d("MyLog", spec.toString())
        init()
        return binding.root
    }

    private fun init() {
        binding.apply {
            for (idx in adapterList.indices) {
                initRV(adapterList[idx], RVlist[idx])
            }
        }
    }

    private fun initRV (adapter: SpecificationsListAdapter, RView: RecyclerView) {
        binding.apply {
            RView.adapter = adapter
            RView.layoutManager = LinearLayoutManager(this@SpecificationsFragment.context)
        }
    }
}