package com.example.foodorderingappadmin.model

data class UserModel(
    val userPerson: String? = null,
    val resName: String? = null,
    val usEmail: String? = null,
    val usPass: String? = null,
    var phone: String? = null,
    var address: String? = null,
)