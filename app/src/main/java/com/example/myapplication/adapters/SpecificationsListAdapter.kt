package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constance
import com.example.myapplication.R
import com.example.myapplication.classes.Specification
import com.example.myapplication.classes.Tractor
import com.example.myapplication.databinding.SimpleSpecificationsItemBinding

class SpecificationsListAdapter: RecyclerView.Adapter<SpecificationsListAdapter.SpecificationHolder>() {
    private var specificationList = arrayListOf<Specification>()
    lateinit var context: Context

    class SpecificationHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = SimpleSpecificationsItemBinding.bind(item)
        fun bind (spec: Specification) = with(binding){
            textSpecName.text = spec.name
            textSpecValue.text = spec.value
        }
    }

    fun setSpecificationList(list: ArrayList<String>, idx: Int) {
        for (el in list.indices) {
            val simp_spec = Specification()
            simp_spec.name = Constance.specNames[idx][el]
            simp_spec.value = list[el]
            specificationList.add(simp_spec)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_specifications_item, parent, false)
        context = parent.context
        return SpecificationHolder(view)
    }

    override fun onBindViewHolder(holder: SpecificationHolder, position: Int) {
        holder.bind(specificationList[position])
    }

    override fun getItemCount(): Int {
        return specificationList.size
    }
}