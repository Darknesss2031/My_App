package com.example.myapplication.classes

class Equipment (var id: Int, var type: Int) {
    var imageURLList = ArrayList<String>()
    var name = ""
    var shortDesc = ""
    var fullDesc = ""
    var priceList = ArrayList<Int>()
    var availability = ""
    var specifications = ""
    var isBought = false
    var buyCount = 1
}