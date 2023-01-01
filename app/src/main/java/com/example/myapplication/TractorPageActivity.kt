package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.ActivityTractorPageBinding
import com.example.myapplication.fragments.DescriptionFragment
import com.example.myapplication.fragments.SpecificationsFragment
import org.json.JSONObject

class TractorPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityTractorPageBinding
    private var specList = ArrayList<ArrayList<String>>()
    lateinit var curTitle: String
    lateinit var priceList: ArrayList<Int>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityTractorPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        curTitle = intent.getStringExtra("trCorpName") + " " + intent.getStringExtra("trMod")
        supportActionBar?.title = curTitle
        val imageURL = intent.getStringArrayListExtra("trImURL")!!
        val fullDesc = intent.getStringExtra("trFullDesc")
        for (i in 0 until Constance.specSize.size) {
            val cur = intent.getStringArrayListExtra("S$i")
            Log.d("MyLog", cur.toString())
            specList.add(cur!!)
        }
        priceList = intent.getIntegerArrayListExtra("trPrice")!!
        binding.valueAOP.text = rubbles(priceList[0])
        binding.valueARP.text = rubbles(priceList[1])
        binding.valueBuildPrice.text = rubbles(priceList[2])
        val videoURL = intent.getStringExtra("trVideoURL")
        Log.d("MyLog", UserData.name)
        launchFragment(DescriptionFragment(imageURL, fullDesc, videoURL))
        binding.apply {
            butReserve.setOnClickListener {
                onClickReserve()
            }
            navMenuSimp.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.description -> launchFragment(
                        DescriptionFragment(
                            imageURL,
                            fullDesc,
                            videoURL
                        )
                    )
                    R.id.specifications -> launchFragment(SpecificationsFragment(specList))
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
        if (!isNumber(binding.textAmount.text.toString())) {
            displayedText = "Введено неверное количество"
            dialog.setMessage(displayedText)
            dialog.setNegativeButton("Отмена"){ _, i -> }
            dialog.show()
            return
        }
        displayedText = "Вы уверены, что хотите зарезервировать\n${curTitle}\nв количестве ${binding.textAmount.text} шт.\n" +
                "стоимостью ${binding.textAmount.text.toString().toInt() * priceList[0]}₽?"
        dialog.setMessage(displayedText)
        dialog.setPositiveButton("Да") { _, i ->
            val url =
                "https://script.google.com/macros/s/AKfycbwyB4P8-2xZ6OqN50B3XFk9iMA8xj9w9PhqxSbw70oRr5oic9gob13yZb4RNqobZWZf/exec"
            val queue = Volley.newRequestQueue(this)
            val jsonPost = JSONObject()
            jsonPost.put("snp", UserData.name)
            jsonPost.put("phone", UserData.phoneNumber)
            jsonPost.put("model", curTitle)
            jsonPost.put("amount", binding.textAmount.text.toString())
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


