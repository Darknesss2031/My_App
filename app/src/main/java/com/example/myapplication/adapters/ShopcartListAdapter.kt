package com.example.myapplication.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.GlobalList
import com.example.myapplication.R
import com.example.myapplication.TractorPageActivity
import com.example.myapplication.classes.Tractor
import com.example.myapplication.databinding.SimpleShopcartItemBinding
import com.squareup.picasso.Picasso

class ShopcartListAdapter: RecyclerView.Adapter<ShopcartListAdapter.ShopcartHolder>(){
    private var tractorList = arrayListOf<Tractor>()
    lateinit var context: Context

    class ShopcartHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = SimpleShopcartItemBinding.bind(item)
        fun bind (tractor: Tractor) = with(binding){
            Picasso.with(itemView.context).load(tractor.imageURLList.first()).into(tractorImageView)
            val trName = tractor.creatorCorpName + "\n" + tractor.model
            textTrName.text = trName
            val tractorBuildCost = "+ " + rubbles(tractor.priceList.last())
            textTrCost.text = rubbles(tractor.priceList.first())
            buildCostView.text = tractorBuildCost
            editTextAmount.setText(tractor.buyCount.toString())
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

    fun setTractorList(list: ArrayList<Tractor>) {
        for (curTractor in list) {
            if (curTractor.isBought) {
                tractorList.add(curTractor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopcartHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_shopcart_item, parent, false)
        context = parent.context
        return ShopcartHolder(view)
    }

    override fun onBindViewHolder(holder: ShopcartHolder, position: Int) {
        holder.bind(tractorList[position])
        holder.binding.viewOpen.setOnClickListener {
            onClickItem(tractorList[position])
        }
        holder.binding.butInc.setOnClickListener {
            var displayedString = clearString(holder.binding.editTextAmount.text.toString())
            if (displayedString == "") {
                holder.binding.editTextAmount.setText("1")
                GlobalList.tractorList[tractorList[position].id].buyCount = 1
                tractorList[position].buyCount = 1
            } else {
                displayedString = (displayedString.toInt() + 1).toString()
                holder.binding.editTextAmount.setText(displayedString)
                GlobalList.tractorList[tractorList[position].id].buyCount = displayedString.toInt()
                tractorList[position].buyCount = displayedString.toInt()
            }
        }
        holder.binding.butDec.setOnClickListener {
            var displayedString = clearString(holder.binding.editTextAmount.text.toString())
            if (displayedString == "") {
                holder.binding.editTextAmount.setText("1")
                GlobalList.tractorList[tractorList[position].id].buyCount = 1
                tractorList[position].buyCount = 1
            } else if (displayedString == "1") {
                val dialog = AlertDialog.Builder(this.context)
                dialog.setTitle("Подтверждение")
                val displayedText = "Вы уверены, что хотите убрать данный трактор из корзины?"
                dialog.setMessage(displayedText)
                dialog.setPositiveButton("Да"){
                    _, i ->
                    run {
                        GlobalList.tractorList[tractorList[position].id].isBought = false
                        tractorList.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
                dialog.setNegativeButton("Отмена"){ _, i -> }
                dialog.show()
            } else {
                displayedString = (displayedString.toInt() - 1).toString()
                holder.binding.editTextAmount.setText(displayedString)
                GlobalList.tractorList[tractorList[position].id].buyCount = displayedString.toInt()
                tractorList[position].buyCount = displayedString.toInt()
            }
        }
    }

    override fun getItemCount(): Int {
        return tractorList.size
    }

    private fun onClickItem(item: Tractor) {
        val intent = Intent(this.context, TractorPageActivity::class.java)
        intent.putExtra("trId", item.id)
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