package com.example.myapplication

import android.widget.TextView
import com.example.myapplication.classes.Equipment
import com.example.myapplication.classes.Tractor

object GlobalList {
    val tractorList = arrayListOf<Tractor>()
    val extraList = arrayListOf<ArrayList<Equipment>>(
        arrayListOf<Equipment>(),
        arrayListOf<Equipment>(),
        arrayListOf<Equipment>(),
        arrayListOf<Equipment>(),
        arrayListOf<Equipment>(),
        arrayListOf<Equipment>()
    )
    /*val frontList = arrayListOf<Equipment>()
    val frontExtraList = arrayListOf<Equipment>()
    val pressList = arrayListOf<Equipment>()
    val frezList = arrayListOf<Equipment>()
    val excList = arrayListOf<Equipment>()
    val snowList = arrayListOf<Equipment>()*/
}