package com.example.foodorderingappadmin

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingappadmin.adapter.OrderDetailsFullItemAdapter
import com.example.foodorderingappadmin.databinding.ActivityOrderDetailsBinding
import com.example.foodorderingappadmin.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ActivityOrderDetails : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding

    private var name: String? = null
    private var address: String? = null
    private var phoneNum: String? = null
    private var totalAmount: String? = null
    private var foodName: ArrayList<String> = arrayListOf()
    private var foodPrice: ArrayList<String> = arrayListOf()

    // private var foodDescription: MutableList<String> = mutableListOf()
    private var foodImage: ArrayList<String> = arrayListOf()

    // private var foodIngredients: MutableList<String> = mutableListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setSafeOnClickListener {
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("userOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetails ->
            name = receivedOrderDetails.userPerson
            foodName = receivedOrderDetails.foodName as ArrayList<String>
            foodImage = receivedOrderDetails.foodImage as ArrayList<String>
            foodQuantity = receivedOrderDetails.foodQuantity as ArrayList<Int>
            address = receivedOrderDetails.address
            phoneNum = receivedOrderDetails.phone
            foodPrice = receivedOrderDetails.foodPrice as ArrayList<String>
            totalAmount = receivedOrderDetails.totalPrice
            setUserDetails()
            setAdapter()

        }
    }

    private fun setAdapter() {
        binding.rvOrderDetails.layoutManager = LinearLayoutManager(this)
        val adapter =
            OrderDetailsFullItemAdapter(this, foodName, foodImage, foodQuantity, foodPrice)
        binding.rvOrderDetails.adapter = adapter
    }

    private fun setUserDetails() {
        binding.etName.text = name
        binding.etAddress.text = address
        binding.etPhone.text = phoneNum
        binding.etTotalAmountNum.text = totalAmount
    }
}