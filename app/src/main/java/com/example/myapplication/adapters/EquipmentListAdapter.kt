package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constance
import com.example.myapplication.EquipmentPageActivity
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
            Picasso.with(itemView.context).load(eqItem.imageURLList.first()).into(tractorImage)
            tractorNameView.text = eqItem.name
            tractorShortDesc.text = eqItem.shortDesc
            tractorPriceView.text = rubbles(eqItem.priceList.first())
            if (eqItem.availability == Constance.availabilities.first()) {
                textAvailabilityR.visibility = View.VISIBLE
                textAvailabilityR.text = eqItem.availability
            } else {
                textAvailabilityNR.visibility = View.VISIBLE
                textAvailabilityNR.text = eqItem.availability
            }
        }
        private fun rubbles(num: Int): String {
            var total = ""
            val temp = num.toString()
            var counter = temp.length
            while (counter - 3 > 0) {
                total = temp.substring(counter - 3, counter) + " " + total
                counter -= 3
            }
            total = temp.substring(0, counter) + " " + total
            return "${total} ₽"
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
        holder.binding.butMore.setOnClickListener {
            onClickItem(equipmentList[position])
        }
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }

    private fun onClickItem(item: Equipment) {
        val intent = Intent(this.context, EquipmentPageActivity::class.java)
        intent.putExtra("eqId", item.id.toString())
        intent.putExtra("eqImURL", item.imageURLList)
        intent.putExtra("eqType", item.type)
        intent.putExtra("eqName", item.name)
        intent.putExtra("eqFullDesc", item.fullDesc)
        intent.putExtra("eqPrice", item.priceList)
        intent.putExtra("eqSpec", item.specifications)
        context.startActivity(intent)
    }
}