package com.example.foodorderingapp.model

data class CartModel(
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null,
    val foodQuantity: Int? = null,
    val foodIngredients: String? = null,
)