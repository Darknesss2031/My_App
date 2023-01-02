package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.Global
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.classes.Equipment
import com.example.myapplication.classes.Tractor
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.fragments.*
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute.Use
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val tractorDataList = arrayListOf<Tractor>()
    private var UserDataStorage: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        UserDataStorage = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        UserData.name = UserDataStorage?.getString("name", "null")!!
        UserData.phoneNumber = UserDataStorage?.getString("phone", "null")!!
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tractorDataList.clear()
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
            launchFragment(CatalogFragment())
            if (UserData.name == "null" || UserData.phoneNumber == "null") { getUserData() }
        }, 3000)
        binding.apply {
            navMenu.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.catalog -> launchFragment(CatalogFragment())
                    R.id.extra -> launchFragment(EquipmentFragment())
                    R.id.shopcart -> launchFragment(ShopcartFragment())
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
        //val url = "https://script.google.com/macros/s/AKfycbyD-eyUBxx-Pai_3rMB8U1Ynyse7xstQgsK8GXSin6xUD8tRufG5UVa6qJ3BGSb4yJXVA/exec"
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
            curTractor.availability = Constance.availabilities[curMap.getInt("Availability")]
            curTractor.videoUrl = curMap.getString("VideoLink")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curTractor.priceList.add(priceList.getInt(idx))
            }
            for (i in 1..10) {
                val specArray = curMap.getJSONArray("S$i")
                for (el in 0 until specArray.length()) {
                    curTractor.spec[i - 1].add(specArray.getString(el))
                }
            }
            GlobalList.tractorList.add(curTractor)
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
        val frontExtraDataJSON = eqData.getJSONArray("frontExtra")
        val pressDataJSON = eqData.getJSONArray("press")
        val frezDataJSON = eqData.getJSONArray("frez")
        val excDataJSON = eqData.getJSONArray("exc")
        val snowDataJSON = eqData.getJSONArray("snow")

        for (counter in 0 until frontDataJSON.length()) {
            val curMap = JSONObject(frontDataJSON.getString(counter))
            val curEq = Equipment(curMap.getInt("Id"), "A")
            val imageList = curMap.getJSONArray("ImageURL")
            for (idx in 0 until imageList.length()) {
                if (imageList.get(idx) != 0) {
                    curEq.imageURLList.add(imageList.getString(idx))
                }
            }
            curEq.name = curMap.getString("Name")
            curEq.shortDesc = curMap.getString("ShDesc")
            curEq.fullDesc = curMap.getString("FullDesc")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curEq.priceList.add(priceList.getInt(idx))
            }
            curEq.availability = Constance.availabilities[curMap.getInt("Availability")]
            curEq.specifications = curMap.getString("Specifications")
            GlobalList.frontList.add(curEq)
        }

        for (counter in 0 until frontExtraDataJSON.length()) {
            val curMap = JSONObject(frontExtraDataJSON.getString(counter))
            val curEq = Equipment(curMap.getInt("Id"), "B")
            val imageList = curMap.getJSONArray("ImageURL")
            for (idx in 0 until imageList.length()) {
                if (imageList.get(idx) != 0) {
                    curEq.imageURLList.add(imageList.getString(idx))
                }
            }
            curEq.name = curMap.getString("Name")
            curEq.shortDesc = curMap.getString("ShDesc")
            curEq.fullDesc = curMap.getString("FullDesc")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curEq.priceList.add(priceList.getInt(idx))
            }
            curEq.availability = Constance.availabilities[curMap.getInt("Availability")]
            curEq.specifications = curMap.getString("Specifications")
            GlobalList.frontExtraList.add(curEq)
        }

        for (counter in 0 until pressDataJSON.length()) {
            val curMap = JSONObject(pressDataJSON.getString(counter))
            val curEq = Equipment(curMap.getInt("Id"), "C")
            val imageList = curMap.getJSONArray("ImageURL")
            for (idx in 0 until imageList.length()) {
                if (imageList.get(idx) != 0) {
                    curEq.imageURLList.add(imageList.getString(idx))
                }
            }
            curEq.name = curMap.getString("Name")
            curEq.shortDesc = curMap.getString("ShDesc")
            curEq.fullDesc = curMap.getString("FullDesc")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curEq.priceList.add(priceList.getInt(idx))
            }
            curEq.availability = Constance.availabilities[curMap.getInt("Availability")]
            curEq.specifications = curMap.getString("Specifications")
            GlobalList.pressList.add(curEq)
        }

        for (counter in 0 until frezDataJSON.length()) {
            val curMap = JSONObject(frezDataJSON.getString(counter))
            val curEq = Equipment(curMap.getInt("Id"), "C")
            val imageList = curMap.getJSONArray("ImageURL")
            for (idx in 0 until imageList.length()) {
                if (imageList.get(idx) != 0) {
                    curEq.imageURLList.add(imageList.getString(idx))
                }
            }
            curEq.name = curMap.getString("Name")
            curEq.shortDesc = curMap.getString("ShDesc")
            curEq.fullDesc = curMap.getString("FullDesc")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curEq.priceList.add(priceList.getInt(idx))
            }
            curEq.availability = Constance.availabilities[curMap.getInt("Availability")]
            curEq.specifications = curMap.getString("Specifications")
            GlobalList.frezList.add(curEq)
        }

        for (counter in 0 until excDataJSON.length()) {
            val curMap = JSONObject(excDataJSON.getString(counter))
            val curEq = Equipment(curMap.getInt("Id"), "D")
            val imageList = curMap.getJSONArray("ImageURL")
            for (idx in 0 until imageList.length()) {
                if (imageList.get(idx) != 0) {
                    curEq.imageURLList.add(imageList.getString(idx))
                }
            }
            curEq.name = curMap.getString("Name")
            curEq.shortDesc = curMap.getString("ShDesc")
            curEq.fullDesc = curMap.getString("FullDesc")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curEq.priceList.add(priceList.getInt(idx))
            }
            curEq.availability = Constance.availabilities[curMap.getInt("Availability")]
            curEq.specifications = curMap.getString("Specifications")
            GlobalList.excList.add(curEq)
        }

        for (counter in 0 until snowDataJSON.length()) {
            val curMap = JSONObject(snowDataJSON.getString(counter))
            val curEq = Equipment(curMap.getInt("Id"), "E")
            val imageList = curMap.getJSONArray("ImageURL")
            for (idx in 0 until imageList.length()) {
                if (imageList.get(idx) != 0) {
                    curEq.imageURLList.add(imageList.getString(idx))
                }
            }
            curEq.name = curMap.getString("Name")
            curEq.shortDesc = curMap.getString("ShDesc")
            curEq.fullDesc = curMap.getString("FullDesc")
            val priceList = curMap.getJSONArray("Price")
            for (idx in 0 until priceList.length()) {
                curEq.priceList.add(priceList.getInt(idx))
            }
            curEq.availability = Constance.availabilities[curMap.getInt("Availability")]
            curEq.specifications = curMap.getString("Specifications")
            GlobalList.snowList.add(curEq)
        }
    }

    private fun getUserData() {
        val intent = Intent(this, GetUserDataActivity::class.java)
        this.startActivity(intent)
    }

}