package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.GlobalList
import com.example.myapplication.classes.Equipment
import com.example.myapplication.adapters.EquipmentListAdapter
import com.example.myapplication.databinding.FragmentEquipmentBinding

class EquipmentFragment : Fragment() {

    lateinit var binding: FragmentEquipmentBinding
    private var frontAdapter = EquipmentListAdapter()
    private var frontExtraAdapter = EquipmentListAdapter()
    private var pressAdapter = EquipmentListAdapter()
    private var frezAdapter = EquipmentListAdapter()
    private var excAdapter = EquipmentListAdapter()
    private var snowAdapter = EquipmentListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        frontAdapter.setEquipmentList(GlobalList.frontList)
        frontExtraAdapter.setEquipmentList(GlobalList.frontExtraList)
        pressAdapter.setEquipmentList(GlobalList.pressList)
        frezAdapter.setEquipmentList(GlobalList.frezList)
        excAdapter.setEquipmentList(GlobalList.excList)
        snowAdapter.setEquipmentList(GlobalList.snowList)
        binding = FragmentEquipmentBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        initRV(frontAdapter, binding.frontView, binding.butFront)
        initRV(frontExtraAdapter, binding.frontExtra, binding.butFrontExtra)
        initRV(pressAdapter, binding.pressView, binding.butPress)
        initRV(frezAdapter, binding.frezView, binding.butFrez)
        initRV(excAdapter, binding.excView, binding.butExc)
        initRV(snowAdapter, binding.snowView, binding.butSnow)
    }

    private fun initRV (adapter: EquipmentListAdapter, RView: RecyclerView, but: Button) {
        binding.apply {
            RView.adapter = adapter
            RView.layoutManager = LinearLayoutManager(this@EquipmentFragment.context)
            but.setOnClickListener {
                if (RView.visibility == View.GONE) {
                    RView.visibility = View.VISIBLE
                } else {
                    RView.visibility = View.GONE
                }
            }
        }
    }
}