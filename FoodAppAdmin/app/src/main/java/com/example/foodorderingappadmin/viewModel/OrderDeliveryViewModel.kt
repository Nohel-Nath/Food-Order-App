package com.example.foodorderingappadmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodorderingappadmin.model.OrderDetails
import com.google.firebase.database.*

class OrderDeliveryViewModel:ViewModel() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val _completedOrders = MutableLiveData<List<OrderDetails>>()  // private mutable
    val completedOrders: LiveData<List<OrderDetails>> = _completedOrders

    fun fetchCompletedOrders() {
        val completeOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")

        completeOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<OrderDetails>()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(OrderDetails::class.java)
                    order?.let { orderList.add(it) }
                }
                orderList.reverse() // latest order first
                _completedOrders.postValue(orderList)
            }

            override fun onCancelled(error: DatabaseError) {
                _completedOrders.postValue(emptyList()) // error হলে খালি লিস্ট দিব
            }
        })
    }
}