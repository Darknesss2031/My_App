package com.example.myapplication.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.CommentActivity
import com.example.myapplication.Constance
import com.example.myapplication.GlobalList
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.FragmentShopcartBinding
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute.Use
import org.json.JSONObject
import java.util.*

class ShopcartFragment : Fragment() {

    companion object {
        lateinit var binding: FragmentShopcartBinding
        fun refreshCostAmount() {
            var costAmount = 0
            for (curTr in GlobalList.tractorList) {
                if (curTr.isBought) {
                    costAmount += curTr.priceList.first() * curTr.buyCount
                }
                if (curTr.isBuild) {
                    costAmount += curTr.priceList.last() * curTr.buyCount
                }
            }
            for (type in GlobalList.extraList) {
                for (curEq in type) {
                    if (curEq.isBought) {
                        costAmount += curEq.priceList.first() * curEq.buyCount
                    }
                }
            }
            binding.textTotalCost.text = rubbles(costAmount)
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

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopcartBinding.inflate(layoutInflater)
        refreshCostAmount()
        binding.butOrder.setOnClickListener {
            if (binding.textTotalCost.text.toString() == rubbles(0)) {
                val text = "Ваша корзина пуста!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this.context, text, duration)
                toast.show()
            } else if (UserData.name == "null" || UserData.phoneNumber == "null" || UserData.corpName == "null") {
                val dialog = AlertDialog.Builder(this.context)
                dialog.setTitle("Авторизация")
                val displayedText: String
                displayedText = "Заполните информацию о себе в разделе Информация"
                dialog.setMessage(displayedText)
                dialog.setNegativeButton("Ок"){ _, i -> }
                dialog.show()
            } else {
                val intent = Intent(this.context, CommentActivity::class.java)
                context?.startActivity(intent)
            }
        }
        return binding.root
    }

}