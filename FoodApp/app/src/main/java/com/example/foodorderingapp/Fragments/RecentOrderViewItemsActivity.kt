package com.example.foodorderingapp.Fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.RecentOrderItemsAdapter
import com.example.foodorderingapp.databinding.ActivityRecentOrderViewItemsBinding
import com.example.foodorderingapp.model.OrderDetailsModel
import com.example.foodorderingapp.setSafeOnClickListener

class RecentOrderViewItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecentOrderViewItemsBinding
    private lateinit var allFoodNames: ArrayList<String>
    private lateinit var allFoodImages: ArrayList<String>
    private lateinit var allFoodPrices: ArrayList<String>
    private lateinit var allFoodQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentOrderViewItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setSafeOnClickListener {
            finish()
        }

        val recentOrderItems =
            intent.getSerializableExtra("RecentBuyOrderItems") as? ArrayList<OrderDetailsModel>
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]
                allFoodNames = recentOrderItem.foodName as ArrayList<String>
                allFoodImages = recentOrderItem.foodImage as ArrayList<String>
                allFoodPrices = recentOrderItem.foodPrice as ArrayList<String>
                allFoodQuantities = recentOrderItem.foodQuantity as ArrayList<Int>
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.pendingOrderRecycleView
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentOrderItemsAdapter(
            this,
            allFoodNames,
            allFoodImages,
            allFoodPrices,
            allFoodQuantities
        )
        rv.adapter = adapter
    }
}