package com.example.myapplication

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.classes.Equipment
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.ActivityEquipmentPageBinding
import com.example.myapplication.fragments.DescriptionFragment
import com.example.myapplication.fragments.EquipmentSpecFragment

class EquipmentPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityEquipmentPageBinding
    lateinit var curTitle: String
    lateinit var curEq: Equipment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityEquipmentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val eqId = intent?.getIntExtra("eqId", 0)!!
        curEq = GlobalList.extraList[intent.getIntExtra("eqType", 0)][eqId]
        curTitle = curEq.name
        supportActionBar?.title = curEq.name
        launchFragment(DescriptionFragment(curEq.imageURLList, curEq.fullDesc))
        binding.valueAOP.text = rubbles(curEq.priceList.first())
        binding.valueARP.text = rubbles(curEq.priceList.last())
        if (curEq.isBought) {
            turnButtonsOff()
        } else {
            turnButtonsOn()
        }
        binding.apply {
            navMenuSimp.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.description -> launchFragment(DescriptionFragment(curEq.imageURLList, curEq.fullDesc))
                    R.id.specifications -> launchFragment(EquipmentSpecFragment(curEq.specifications))
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

    private fun onClickReserve() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Подтверждение")
        val displayedText: String
        if (!isNumber(binding.textAmountEq.text.toString()) ||
            binding.textAmountEq.text.toString() == "0" ||
            binding.textAmountEq.text.toString() == "") {
            displayedText = "Введено неверное количество"
            dialog.setMessage(displayedText)
            dialog.setNegativeButton("Отмена"){ _, i -> }
            dialog.show()
            return
        } else if (curEq.availability != Constance.availabilities.first()) {
            displayedText = "Данного трактора нет в наличии"
            dialog.setMessage(displayedText)
            dialog.setNegativeButton("Отмена"){ _, i -> }
            dialog.show()
            return
        }
        displayedText =
            "Вы уверены, что хотите добавить в корзину\n${curTitle}\nв количестве ${binding.textAmountEq.text} шт.?"
        val amount = if (binding.textAmountEq.text.toString() == "") {
            "1"
        } else {
            binding.textAmountEq.text.toString()
        }
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
            jsonPost.put("amount", binding.textAmountEq.text.toString())
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
            GlobalList.extraList[curEq.type][curEq.id].isBought = true
            GlobalList.extraList[curEq.type][curEq.id].buyCount = amount.toInt()
            turnButtonsOff()
        }
        dialog.setNegativeButton("Отмена") { _, i -> }
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

    private fun turnButtonsOff() {
        binding.butBig.setOnClickListener {  }
        binding.butSm.setOnClickListener {  }
        binding.butReserveEq.setOnClickListener {  }
        binding.butBig.visibility = View.GONE
        binding.butSm.visibility = View.GONE
        binding.textAmountEq.visibility = View.GONE
        binding.butReserveEq.visibility = View.INVISIBLE
        binding.textInCart.visibility = View.VISIBLE
    }

    private fun turnButtonsOn() {
        binding.apply {
            butReserveEq.setOnClickListener {
                onClickReserve()
            }
            butBig.setOnClickListener {
                var displayedString = clearString(binding.textAmountEq.text.toString())
                if (displayedString == "") {
                    textAmountEq.setText("1")
                    GlobalList.extraList[curEq.type][curEq.id].buyCount = 1
                    curEq.buyCount = 1
                } else {
                    displayedString = (displayedString.toInt() + 1).toString()
                    textAmountEq.setText(displayedString)
                    GlobalList.extraList[curEq.type][curEq.id].buyCount = displayedString.toInt()
                    curEq.buyCount = displayedString.toInt()
                }
            }
            butSm.setOnClickListener {
                var displayedString = textAmountEq.text.toString()
                if (displayedString == "") {
                    textAmountEq.setText("1")
                    GlobalList.extraList[curEq.type][curEq.id].buyCount = 1
                    curEq.buyCount = 1
                } else if (displayedString != "0") {
                    displayedString = (displayedString.toInt() - 1).toString()
                    textAmountEq.setText(displayedString)
                    GlobalList.extraList[curEq.type][curEq.id].buyCount = displayedString.toInt()
                    curEq.buyCount = displayedString.toInt()
                }
            }
        }
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