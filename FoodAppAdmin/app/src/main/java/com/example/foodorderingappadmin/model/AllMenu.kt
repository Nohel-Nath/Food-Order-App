package com.example.foodorderingappadmin.model

data class AllMenu(
    val foodName :String? = null,
    val foodPrice :String? = null,
    val foodDescription :String? = null,
    val foodImage :String? = null,
    val foodIngredients :String? = null,
    var key: String = "" // Firebase key রাখার জন্য
)
