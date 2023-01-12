package com.example.myapplication

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.ActivityCommentBinding
import org.json.JSONObject
import java.util.*

class CommentActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.button.setOnClickListener {
            val curTime = Calendar.getInstance().time.toString()
            var sendTrStr = ""
            var amountTr = ""
            var sendEqStr = ""
            var amountEq = ""
            val url =
                "https://script.google.com/macros/s/AKfycbwyB4P8-2xZ6OqN50B3XFk9iMA8xj9w9PhqxSbw70oRr5oic9gob13yZb4RNqobZWZf/exec"
            val queue = Volley.newRequestQueue(this)
            val jsonPost = JSONObject()
            jsonPost.put("snp", UserData.name)
            jsonPost.put("phone", UserData.phoneNumber)
            jsonPost.put("corpName", UserData.corpName)
            jsonPost.put("comm", binding.editTextComms.text.toString())
            for (curTr in GlobalList.tractorList) {
                var build = ""
                if (curTr.isBuild) {
                    build = Constance.withBuild
                } else {
                    build = Constance.withoutBuild
                }
                if (curTr.isBought) {
                    sendTrStr += curTr.creatorCorpName + " " + curTr.model + " " + build + Constance.separator
                    amountTr += curTr.buyCount.toString() + Constance.separator
                }
            }
            for (type in GlobalList.extraList) {
                for (curEq in type) {
                    if (curEq.isBought) {
                        sendEqStr += curEq.name + Constance.separator
                        amountEq += curEq.buyCount.toString() + Constance.separator
                    }
                }
            }
            jsonPost.put("modelTr", sendTrStr)
            jsonPost.put("amountTr", amountTr)
            jsonPost.put("modelEq", sendEqStr)
            jsonPost.put("amountEq", amountEq)
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
                        val toast = Toast.makeText(this, text, duration)
                        toast.show()
                    }
                },
            )
            queue.add(request)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Заказ оформлен!")
            val displayedText: String
            displayedText = "Ваш заказ успешно оформлен!\nМы свяжемся с вами в ближайшее время!"
            dialog.setMessage(displayedText)
            dialog.setNegativeButton("Ок"){ _, i -> finish()}
            dialog.show()
        }
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finishActivity(RESULT_CANCELED)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}