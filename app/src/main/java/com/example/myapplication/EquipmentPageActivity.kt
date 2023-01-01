package com.example.myapplication

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.ActivityEquipmentPageBinding
import com.example.myapplication.fragments.DescriptionFragment
import com.example.myapplication.fragments.EquipmentSpecFragment
import com.example.myapplication.fragments.SpecificationsFragment
import org.json.JSONObject

class EquipmentPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityEquipmentPageBinding
    lateinit var curTitle: String
    lateinit var priceList: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityEquipmentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        curTitle = intent.getStringExtra("eqName").toString()
        supportActionBar?.title = curTitle
        val imageURL = intent.getStringArrayListExtra("eqImURL")!!
        val fullDesc = intent.getStringExtra("eqFullDesc")
        launchFragment(DescriptionFragment(imageURL, fullDesc))
        priceList = intent.getIntegerArrayListExtra("eqPrice")!!
        binding.valueAOP.text = priceList[0].toString()
        binding.valueARP.text = priceList[1].toString()
        val specifications = intent.getStringExtra("eqSpec")!!
        binding.apply {
            butReserveEq.setOnClickListener {
                onClickReserve()
            }
            navMenuSimp.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.description -> launchFragment(DescriptionFragment(imageURL, fullDesc))
                    R.id.specifications -> launchFragment(EquipmentSpecFragment(specifications))
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

    private fun launchFragment(frag : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.descFrag, frag).commit()
    }

    private fun onClickReserve() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Подтверждение")
        val displayedText: String
        if (!isNumber(binding.textAmountEq.text.toString())) {
            displayedText = "Введено неверное количество"
            dialog.setMessage(displayedText)
            dialog.setNegativeButton("Отмена"){ _, i -> }
            dialog.show()
            return
        }
        displayedText = "Вы уверены, что хотите зарезервировать\n${curTitle}\nв количестве ${binding.textAmountEq.text} шт.\n" +
                "стоимостью ${binding.textAmountEq.text.toString().toInt() * priceList[0]}₽?"
        dialog.setMessage(displayedText)
        dialog.setPositiveButton("Да") { _, i ->
            val url =
                "https://script.google.com/macros/s/AKfycbwyB4P8-2xZ6OqN50B3XFk9iMA8xj9w9PhqxSbw70oRr5oic9gob13yZb4RNqobZWZf/exec"
            val queue = Volley.newRequestQueue(this)
            val jsonPost = JSONObject()
            jsonPost.put("snp", UserData.name)
            jsonPost.put("phone", UserData.phoneNumber)
            jsonPost.put("model", curTitle)
            jsonPost.put("amount", binding.textAmountEq.text.toString())
            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonPost,
                { _ ->
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
                {
                        result ->
                },
            )
            queue.add(request)
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
        return "${num}₽"
    }
}