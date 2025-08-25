package com.example.foodorderingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderingapp.adapter.CartAdapter
import com.example.foodorderingapp.databinding.ActivityPayOutBinding
import com.example.foodorderingapp.model.OrderDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOutBinding
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phoneNum: String
    private lateinit var totalAmount: String
    private lateinit var auth: FirebaseAuth
    private lateinit var foodName: ArrayList<String>
    private lateinit var foodPrice: ArrayList<String>
    private lateinit var foodDescription: ArrayList<String>
    private lateinit var foodImageUri: ArrayList<String>
    private lateinit var foodIngredients: ArrayList<String>
    private lateinit var foodQuantities: ArrayList<Int>
    private lateinit var userId: String
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        setUserData()

        val intent = intent
        foodName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodImageUri = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodIngredients =
            intent.getStringArrayListExtra("FoodMenuItemIngredients") as ArrayList<String>
        foodQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") as ArrayList<Int>

        totalAmount = calculateTotalAmount().toString() + "$"
        binding.tvAmountValue.isEnabled = false
        binding.tvAmountValue.setText(totalAmount)

        binding.btPlaceOrder.setSafeOnClickListener {
            name = binding.etName.text.toString().trim()
            address = binding.etAddress.text.toString().trim()
            phoneNum = binding.etPhone.text.toString().trim()
            if (name.isBlank() && address.isBlank() && phoneNum.isBlank()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_LONG).show()
            } else {
                placeOrder()
            }
        }

        binding.btnBack.setSafeOnClickListener {
            finish()
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetailsModel(
            userId,
            name,
            foodName,
            foodImageUri,
            foodPrice,
            foodQuantities,
            address,
            totalAmount,
            phoneNum,
            false,
            false,
            itemPushKey,
            time
        )
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()
            // finish()
            addOrderToHistory(orderDetails)
        }
            .addOnFailureListener {
                Toast.makeText(this, "failed to order", Toast.LENGTH_LONG).show()
            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetailsModel) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemReference = databaseReference.child("user").child(userId).child("CatItems")
        cartItemReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodPrice.size) {
            var price = foodPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '$') {
                price.dropLast(1).toInt()
            } else {
                price.toInt()
            }
            var quantity = foodQuantities[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("userPerson").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""
                        binding.etName.setText(names)
                        binding.etAddress.setText(addresses)
                        binding.etPhone.setText(phones)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}