package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.ActivityGetUserDataBinding
import com.example.myapplication.fragments.GeninfoFragment

class GetUserDataActivity : AppCompatActivity() {
    lateinit var binding: ActivityGetUserDataBinding
    private var UserDataStorage: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserDataStorage = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        binding = ActivityGetUserDataBinding.inflate(layoutInflater)
        binding.butSave.setOnClickListener{
            onClickDone(binding.butSave)
        }
        setContentView(binding.root)
    }

    fun onClickDone(view: View) {
        binding.apply {
            if (checkField(fieldName) && checkField(fieldSurname) && checkField(fieldPatronymic) && checkField(fieldPhone)) {
                val storageEditor = UserDataStorage?.edit()

                UserData.name = fieldSurname.text.toString() + " " +
                        fieldName.text.toString() + " " +
                        fieldPatronymic.text.toString()
                UserData.phoneNumber = fieldPhone.text.toString()
                storageEditor?.putString("name", UserData.name)
                storageEditor?.putString("phone", UserData.phoneNumber)
                storageEditor?.apply()
                finish()
            } else {
                textError.visibility = View.VISIBLE
            }
        }
    }

    fun checkField (field: EditText): Boolean {
        return field.text.toString() != ""
    }
}