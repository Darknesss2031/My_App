package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EquipmentPageActivity
import com.example.myapplication.GlobalList
import com.example.myapplication.R
import com.example.myapplication.classes.Equipment
import com.example.myapplication.databinding.SimpleShopcartEquipmentItemBinding
import com.example.myapplication.fragments.ShopcartFragment
import com.squareup.picasso.Picasso

class ShopcartEquipmentListAdapter: RecyclerView.Adapter<ShopcartEquipmentListAdapter.ShopcartEquipmentHolder>(){
    private var equipmentList = arrayListOf<Equipment>()
    lateinit var context: Context

    class ShopcartEquipmentHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = SimpleShopcartEquipmentItemBinding.bind(item)
        fun bind (eq: Equipment) = with(binding){
            Picasso.with(itemView.context).load(eq.imageURLList.first()).into(eqImageView)
            textEqName.text = eq.name
            textEqCost.text = rubbles(eq.priceList.first())
            editTextAmount.setText(eq.buyCount.toString())
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

    fun setTractorList(list: ArrayList<ArrayList<Equipment>>) {
        for (type in list) {
            for (curEq in type) {
                if (curEq.isBought) {
                    equipmentList.add(curEq)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopcartEquipmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_shopcart_equipment_item, parent, false)
        context = parent.context
        return ShopcartEquipmentHolder(view)
    }

    override fun onBindViewHolder(holder: ShopcartEquipmentHolder, position: Int) {
        holder.bind(equipmentList[position])
        holder.binding.viewOpen.setOnClickListener {
            onClickItem(equipmentList[position])
        }
        holder.binding.butInc.setOnClickListener {
            var displayedString = clearString(holder.binding.editTextAmount.text.toString())
            if (displayedString == "") {
                holder.binding.editTextAmount.setText("1")
                GlobalList.extraList[equipmentList[position].type][equipmentList[position].id].buyCount = 1
                equipmentList[position].buyCount = 1
            } else {
                displayedString = (displayedString.toInt() + 1).toString()
                holder.binding.editTextAmount.setText(displayedString)
                GlobalList.extraList[equipmentList[position].type][equipmentList[position].id].buyCount = displayedString.toInt()
                equipmentList[position].buyCount = displayedString.toInt()
            }
            ShopcartFragment.refreshCostAmount()
        }
        holder.binding.butDec.setOnClickListener {
            var displayedString = clearString(holder.binding.editTextAmount.text.toString())
            if (displayedString == "") {
                holder.binding.editTextAmount.setText("1")
                GlobalList.extraList[equipmentList[position].type][equipmentList[position].id].buyCount = 1
                equipmentList[position].buyCount = 1
            } else if (displayedString == "1" || displayedString == "0") {
                val dialog = AlertDialog.Builder(this.context)
                dialog.setTitle("Подтверждение")
                val displayedText = "Вы уверены, что хотите убрать данное оборудование из корзины?"
                dialog.setMessage(displayedText)
                dialog.setPositiveButton("Да"){
                        _, _ ->
                    run {
                        GlobalList.extraList[equipmentList[position].type][equipmentList[position].id].isBought = false
                        equipmentList[position].isBought = false
                        equipmentList[position].buyCount = 1
                        ShopcartFragment.refreshCostAmount()
                        equipmentList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
                dialog.setNegativeButton("Отмена"){ _, _ -> }
                dialog.show()
            } else {
                displayedString = (displayedString.toInt() - 1).toString()
                holder.binding.editTextAmount.setText(displayedString)
                GlobalList.extraList[equipmentList[position].type][equipmentList[position].id].buyCount = displayedString.toInt()
                equipmentList[position].buyCount = displayedString.toInt()
            }
            ShopcartFragment.refreshCostAmount()
        }
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }

    private fun onClickItem(item: Equipment) {
        val intent = Intent(this.context, EquipmentPageActivity::class.java)
        intent.putExtra("eqId", item.id)
        intent.putExtra("eqType", item.type)
        context.startActivity(intent)
    }

    private fun clearString(inp: String): String {
        var out = ""
        for (idx in inp.indices) {
            if (inp[idx].isDigit()) {
                out += inp[idx]
            }
        }
        return out
    }
}