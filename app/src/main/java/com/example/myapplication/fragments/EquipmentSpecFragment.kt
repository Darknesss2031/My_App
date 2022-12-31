package com.example.myapplication.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEquipmentSpecBinding

class EquipmentSpecFragment(private val specifications: String) : Fragment() {
    lateinit var binding: FragmentEquipmentSpecBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEquipmentSpecBinding.inflate(layoutInflater)
        binding.textSpecifications.text = specifications.split("~").joinToString("\n")
        return binding.root
    }

}