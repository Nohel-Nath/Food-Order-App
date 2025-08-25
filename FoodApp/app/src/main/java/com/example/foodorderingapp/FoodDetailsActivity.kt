package com.example.foodorderingapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.ActivityFoodDetailsBinding
import com.example.foodorderingapp.model.CartModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FoodDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailsBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    private var foodPrice: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

//        val formattedIngredients = foodIngredients
//            ?.split(",") // Split by comma
//            ?.mapIndexed { index, item -> "${index + 1}. ${item.trim()}" }
//            ?.joinToString("\n") // Join with new line

        with(binding) {
            tvFoodName.text = foodName
            tvDescription.text = foodDescription
            tvIngreName.text = foodIngredients
            Glide.with(this@FoodDetailsActivity).load(Uri.parse(foodImage)).into(ivFoodImage)
        }

        binding.btnBack.setSafeOnClickListener {
            finish()
        }

        binding.btnAddToCart.setSafeOnClickListener {
            addItemToCart()
        }

    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""
        val cartItem = CartModel(
            foodName.toString(),
            foodPrice.toString(),
            foodDescription.toString(),
            foodImage.toString(),
            1,
            foodIngredients.toString()
        )
        database.child("user").child(userId).child("CatItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Items added into cart successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Items not added", Toast.LENGTH_LONG).show()
            }
    }
}