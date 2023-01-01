package com.example.myapplication.classes

data class Tractor(var id: Int) {
    var imageURLList = ArrayList<String>()
    var creatorCorpName = ""
    var model = ""
    var shortDesc = ""
    var fullDesc = ""
    var availability = ""
    var priceList = ArrayList<Int>()
    var videoUrl = ""
    var spec = arrayListOf<ArrayList<String>>(
        ArrayList(6),
        ArrayList(10),
        ArrayList(4),
        ArrayList(1),
        ArrayList(2),
        ArrayList(2),
        ArrayList(2),
        ArrayList(7),
        ArrayList(7),
        ArrayList(11),
    )
}