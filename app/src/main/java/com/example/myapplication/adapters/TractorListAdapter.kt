package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constance
import com.example.myapplication.R
import com.example.myapplication.TractorPageActivity
import com.example.myapplication.classes.Tractor
import com.example.myapplication.databinding.SimpleTractorItemBinding
import com.squareup.picasso.Picasso

class TractorListAdapter: RecyclerView.Adapter<TractorListAdapter.TractorHolder>() {
    private var tractorList = arrayListOf<Tractor>()
    lateinit var context: Context

    class TractorHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = SimpleTractorItemBinding.bind(item)
        fun bind (tractor: Tractor) = with(binding){
            Picasso.with(itemView.context).load(tractor.imageURLList.first()).into(binding.tractorImage)
            val fullTractorName = tractor.creatorCorpName + " " + tractor.model
            tractorNameView.text = fullTractorName
            tractorShortDesc.text = tractor.shortDesc
            tractorPriceView.text = rubbles(tractor.priceList.first())
            if (tractor.availability == Constance.availabilities.first()) {
                textAvailabilityR.visibility = View.VISIBLE
                textAvailabilityR.text = tractor.availability
            } else {
                textAvailabilityNR.visibility = View.VISIBLE
                textAvailabilityNR.text = tractor.availability
            }
        }
        private fun rubbles(num: Int): String {
            return "${num}₽"
        }

    }

    fun setTractorList(list: ArrayList<Tractor>) {
        tractorList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TractorHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_tractor_item, parent, false)
        context = parent.context
        return TractorHolder(view)
    }

    override fun onBindViewHolder(holder: TractorHolder, position: Int) {
        holder.bind(tractorList[position])
        holder.binding.butMore.setOnClickListener {
            onClickItem(tractorList[position])
        }
    }

    override fun getItemCount(): Int {
        return tractorList.size
    }

    private fun onClickItem(item: Tractor) {
        val intent = Intent(this.context, TractorPageActivity::class.java)
        intent.putExtra("trId", item.id.toString())
        intent.putExtra("trImURL", item.imageURLList)
        intent.putExtra("trCorpName", item.creatorCorpName)
        intent.putExtra("trMod", item.model)
        intent.putExtra("trShDesc", item.shortDesc)
        intent.putExtra("trFullDesc", item.fullDesc)
        intent.putExtra("trPrice", item.priceList)
        intent.putExtra("trVideoURL", item.videoUrl)
        for (i in item.spec.indices) {
            intent.putExtra("S$i", item.spec[i])
        }
        context.startActivity(intent)
    }

}