package com.example.myapplication

import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.classes.Tractor
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.ActivityTractorPageBinding
import com.example.myapplication.fragments.DescriptionFragment
import com.example.myapplication.fragments.SpecificationsFragment
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TractorPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityTractorPageBinding
    lateinit var curTitle: String
    lateinit var curTractor: Tractor

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityTractorPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        curTractor = GlobalList.tractorList[intent.getIntExtra("trId", 0)]
        curTitle = curTractor.creatorCorpName + " " + curTractor.model
        supportActionBar?.title = curTitle
        binding.valueAOP.text = rubbles(curTractor.priceList[0])
        binding.valueARP.text = rubbles(curTractor.priceList[1])
        binding.valueBuildPrice.text = rubbles(curTractor.priceList[2])
        launchFragment(DescriptionFragment(curTractor.imageURLList, curTractor.fullDesc, curTractor.videoUrl))
        if (curTractor.isBought) {
            turnButtonsOff()
        } else {
            turnButtonsOn()
        }
        binding.apply {
            navMenuSimp.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.description -> launchFragment(
                        DescriptionFragment(
                            curTractor.imageURLList,
                            curTractor.fullDesc,
                            curTractor.videoUrl
                        )
                    )
                    R.id.specifications -> launchFragment(SpecificationsFragment(curTractor.spec))
                }
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finishActivity(RESULT_CANCELED)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.descFrag, frag).commit()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onClickReserve() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Подтверждение")
        val displayedText: String
        if (!isNumber(binding.textAmount.text.toString()) ||
            binding.textAmount.text.toString() == "0" ||
            binding.textAmount.text.toString() == "") {
            displayedText = "Введено неверное количество"
            dialog.setMessage(displayedText)
            dialog.setNegativeButton("Отмена"){ _, i -> }
            dialog.show()
            return
        } else if (curTractor.availability != Constance.availabilities.first()) {
            displayedText = "Данного трактора нет в наличии"
            dialog.setMessage(displayedText)
            dialog.setNegativeButton("Отмена"){ _, i -> }
            dialog.show()
            return
        }
        val amount = if (binding.textAmount.text.toString() == "") {
            "1"
        } else {
            binding.textAmount.text.toString()
        }
        displayedText = "Вы уверены, что хотите добавить в корзину\n${curTitle}\nв количестве ${binding.textAmount.text} шт.?"
        dialog.setMessage(displayedText)
        /*dialog.setPositiveButton("Да") { _, i ->
            val curTime = Calendar.getInstance().time.toString()
            val url =
                "https://script.google.com/macros/s/AKfycbwyB4P8-2xZ6OqN50B3XFk9iMA8xj9w9PhqxSbw70oRr5oic9gob13yZb4RNqobZWZf/exec"
            val queue = Volley.newRequestQueue(this)
            val jsonPost = JSONObject()
            jsonPost.put("snp", UserData.name)
            jsonPost.put("phone", UserData.phoneNumber)
            jsonPost.put("model", curTitle)
            jsonPost.put("amount", binding.textAmount.text.toString())
            jsonPost.put("date", curTime)
            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonPost,
                {
                        _ ->
                },
                {
                        result ->
                    run {
                        val text = "Ошибка. Проверьте\nинтернет соединение!"
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                        }, 2500)
                    }
                },
            )
            queue.add(request)
        }*/
        dialog.setPositiveButton("Да"){ _, i ->
            GlobalList.tractorList[curTractor.id].isBought = true
            GlobalList.tractorList[curTractor.id].buyCount = amount.toInt()
            turnButtonsOff()
            binding.butReserve.text = "Товар в корзине"
        }
        dialog.setNegativeButton("Отмена"){ _, i -> }
        dialog.show()
    }

    private fun isNumber(str: String): Boolean {
        if (str == "") return false
        for (el in str) {
            if (!el.isDigit()){
                return false
            }
        }
        return true
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

    private fun clearString(inp: String): String {
        var out = ""
        for (idx in inp.indices) {
            if (inp[idx].isDigit()) {
                out += inp[idx]
            }
        }
        return out
    }

    private fun turnButtonsOff() {
        binding.butPlus.setOnClickListener {  }
        binding.butMinus.setOnClickListener {  }
        binding.butReserve.setOnClickListener {  }
        binding.butPlus.visibility = View.GONE
        binding.butMinus.visibility = View.GONE
        binding.textAmount.visibility = View.GONE
        binding.butReserve.visibility = View.INVISIBLE
        binding.textAlreadyInCart.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun turnButtonsOn() {
        binding.apply {
            butPlus.setOnClickListener {
                var displayedString = clearString(binding.textAmount.text.toString())
                if (displayedString == "") {
                    textAmount.setText("1")
                    GlobalList.tractorList[curTractor.id].buyCount = 1
                    curTractor.buyCount = 1
                } else {
                    displayedString = (displayedString.toInt() + 1).toString()
                    textAmount.setText(displayedString)
                    GlobalList.tractorList[curTractor.id].buyCount = displayedString.toInt()
                    curTractor.buyCount = displayedString.toInt()
                }
            }
            butMinus.setOnClickListener {
                var displayedString = textAmount.text.toString()
                if (displayedString == "") {
                    textAmount.setText("1")
                    GlobalList.tractorList[curTractor.id].buyCount = 1
                    curTractor.buyCount = 1
                } else if (displayedString != "0") {
                    displayedString = (displayedString.toInt() - 1).toString()
                    textAmount.setText(displayedString)
                    GlobalList.tractorList[curTractor.id].buyCount = displayedString.toInt()
                    curTractor.buyCount = displayedString.toInt()
                }
            }
            butReserve.setOnClickListener {
                onClickReserve()
            }
        }
    }
}


