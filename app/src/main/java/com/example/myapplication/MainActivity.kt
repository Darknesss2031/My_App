package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.fragments.CatalogFragment
import com.example.myapplication.classes.Equipment
import com.example.myapplication.classes.Tractor
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.fragments.GeninfoFragment
import com.example.myapplication.fragments.EquipmentFragment
import com.example.myapplication.fragments.LoadingFragment
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val tractorDataList = arrayListOf<Tractor>()
    private val frontDataList = arrayListOf<Equipment>()
    private val pressDataList = arrayListOf<Equipment>()
    private val frezDataList = arrayListOf<Equipment>()
    private val excDataList = arrayListOf<Equipment>()
    private val snowDataList = arrayListOf<Equipment>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tractorDataList.clear()
        frontDataList.clear()
        getTractorData()
        getEquipmentData()
        binding.navMenu.visibility = View.INVISIBLE
        launchFragment(LoadingFragment())
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            if (tractorDataList.isEmpty()) {
                val text = "Слишком медленное\nинтернет соединение!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
            binding.navMenu.visibility = View.VISIBLE
            launchFragment(CatalogFragment(tractorDataList))
        }, 3000)
        binding.apply {
            navMenu.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.catalog -> launchFragment(CatalogFragment(tractorDataList))
                    R.id.shopcart -> launchFragment(EquipmentFragment(frontDataList, pressDataList, frezDataList, excDataList, snowDataList))
                    R.id.geninfo -> launchFragment(GeninfoFragment())
                }
                true
            }
        }
    }

    private fun launchFragment(frag : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragHolder, frag).commit()
    }

    @SuppressLint("ShowToast")
    private fun getTractorData() {
        //val url = "https://script.google.com/macros/s/AKfycbxgg70hnvnIiwH-StK6P8U103WCO7VqNAQTU8XgWmZZAFH9ujgsT1UulGw9-EgVKRmKMQ/exec"
        val url = "https://script.google.com/macros/s/AKfycbyD-eyUBxx-Pai_3rMB8U1Ynyse7xstQgsK8GXSin6xUD8tRufG5UVa6qJ3BGSb4yJXVA/exec"
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                    result -> parceTractorData(result)
            },
            {
                    _ ->
                run {
                    val text = "Ошибка. Проверьте\nинтернет соединение!"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 2500)
                }
            }
        )
        queue.add(request)
    }

    private fun parceTractorData(result: String) {
        Log.d("MyLog", result)
        val tractorDataJSON = JSONArray(result)
        for (counter in 0 until tractorDataJSON.length()) {
            val curMap = JSONObject(tractorDataJSON.getString(counter))
            val curTractor = Tractor(curMap.getInt("Id"))
            val imageList = curMap.getJSONArray("ImageURL")
            for (idx in 0 until imageList.length()) {
                if (imageList.get(idx) != 0) {
                    curTractor.imageURLList.add(imageList.getString(idx))
                }
            }
            curTractor.creatorCorpName = curMap.getString("CorpName")
            curTractor.model = curMap.getString("Model")
            curTractor.shortDesc = curMap.getString("ShDesc")
            curTractor.fullDesc = curMap.getString("FullDesc")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curTractor.priceList.add(priceList.getInt(idx))
            }
            for (i in 1..10) {
                val specArray = curMap.getJSONArray("S$i")
                Log.d("MyLog", i.toString())
                for (el in 0 until specArray.length()) {
                    curTractor.spec[i - 1].add(specArray.getString(el))
                }
            }
            /*
            var cnt = 0
            for (sizeidx in Constance.specSize.indices) {
                for (idx in 0 until Constance.specSize[sizeidx]) {
                    curTractor.spec[sizeidx][idx] = curMap.getString("s$cnt")
                    cnt++
                }
            }*/
            tractorDataList.add(curTractor)
        }
    }

    private fun getEquipmentData() {
        val url = "https://script.google.com/macros/s/AKfycbwN3V8G4vbcVdplwEAghnPN0bSRXB1pUhy34lFv1h-nlGqZiAbIwjCyza4PrtR8YFHURw/exec"
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                    result -> parceEquipmentData(result)
            },
            {
                    _ ->
                run {
                    val text = "Ошибка. Проверьте\nинтернет соединение!"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 2500)
                }
            }
        )
        queue.add(request)
    }

    private fun parceEquipmentData(result: String) {
        val eqData = JSONObject(result)
        val frontDataJSON = eqData.getJSONArray("front")
        val pressDataJSON = eqData.getJSONArray("press")
        val frezDataJSON = eqData.getJSONArray("frez")
        val excDataJSON = eqData.getJSONArray("exc")
        val snowDataJSON = eqData.getJSONArray("snow")

        for (counter in 0 until frontDataJSON.length()) {
            val curMap = JSONObject(frontDataJSON.getString(counter))
            val curFront = Equipment(curMap.getInt("Id"), "A")
            curFront.imageURL = curMap.getString("ImageURL")
            curFront.name = curMap.getString("Name")
            curFront.shortDesc = curMap.getString("ShDesc")
            curFront.fullDesc = curMap.getString("FullDesc")
            curFront.price = curMap.getInt("Price")
            frontDataList.add(curFront)
        }

        for (counter in 0 until pressDataJSON.length()) {
            val curMap = JSONObject(pressDataJSON.getString(counter))
            val curFront = Equipment(curMap.getInt("Id"), "B")
            curFront.imageURL = curMap.getString("ImageURL")
            curFront.name = curMap.getString("Name")
            curFront.shortDesc = curMap.getString("ShDesc")
            curFront.fullDesc = curMap.getString("FullDesc")
            curFront.price = curMap.getInt("Price")
            pressDataList.add(curFront)
        }

        for (counter in 0 until frezDataJSON.length()) {
            val curMap = JSONObject(frezDataJSON.getString(counter))
            val curFront = Equipment(curMap.getInt("Id"), "C")
            curFront.imageURL = curMap.getString("ImageURL")
            curFront.name = curMap.getString("Name")
            curFront.shortDesc = curMap.getString("ShDesc")
            curFront.fullDesc = curMap.getString("FullDesc")
            curFront.price = curMap.getInt("Price")
            frezDataList.add(curFront)
        }

        for (counter in 0 until excDataJSON.length()) {
            val curMap = JSONObject(excDataJSON.getString(counter))
            val curFront = Equipment(curMap.getInt("Id"), "D")
            curFront.imageURL = curMap.getString("ImageURL")
            curFront.name = curMap.getString("Name")
            curFront.shortDesc = curMap.getString("ShDesc")
            curFront.fullDesc = curMap.getString("FullDesc")
            curFront.price = curMap.getInt("Price")
            excDataList.add(curFront)
        }

        for (counter in 0 until snowDataJSON.length()) {
            val curMap = JSONObject(snowDataJSON.getString(counter))
            val curFront = Equipment(curMap.getInt("Id"), "E")
            curFront.imageURL = curMap.getString("ImageURL")
            curFront.name = curMap.getString("Name")
            curFront.shortDesc = curMap.getString("ShDesc")
            curFront.fullDesc = curMap.getString("FullDesc")
            curFront.price = curMap.getInt("Price")
            snowDataList.add(curFront)
        }
    }
}