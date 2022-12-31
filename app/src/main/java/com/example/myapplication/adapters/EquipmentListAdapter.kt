package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.TractorPageActivity
import com.example.myapplication.classes.Equipment
import com.example.myapplication.databinding.SimpleTractorItemBinding
import com.squareup.picasso.Picasso

class EquipmentListAdapter: RecyclerView.Adapter<EquipmentListAdapter.EquipmentHolder>() {
    private var equipmentList = arrayListOf<Equipment>()
    lateinit var context: Context

    class EquipmentHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = SimpleTractorItemBinding.bind(item)
        fun bind (eqItem: Equipment) = with(binding){
            Picasso.with(itemView.context).load(eqItem.imageURL)
            tractorNameView.text = eqItem.name
            tractorShortDesc.text = eqItem.shortDesc
            tractorPriceView.text = eqItem.price.toString()
        }
    }

    fun setEquipmentList(list: ArrayList<Equipment>) {
        equipmentList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_tractor_item, parent, false)
        context = parent.context
        return EquipmentHolder(view)
    }

    override fun onBindViewHolder(holder: EquipmentHolder, position: Int) {
        holder.bind(equipmentList[position])
        holder.binding.butOpen.setOnClickListener {
            onClickItem(equipmentList[position])
        }
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }

    private fun onClickItem(item: Equipment) {
        val intent = Intent(this.context, TractorPageActivity::class.java)
        intent.putExtra("eqId", item.id.toString())
        intent.putExtra("eqImURL", item.imageURL)
        intent.putExtra("eqType", item.type)
        intent.putExtra("eqName", item.name)
        intent.putExtra("eqShDesc", item.shortDesc)
        intent.putExtra("eqFullDesc", item.fullDesc)
        intent.putExtra("eqPrice", item.price)
        context.startActivity(intent)
    }
}