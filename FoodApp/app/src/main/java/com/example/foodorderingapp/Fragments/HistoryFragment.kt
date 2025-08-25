package com.example.foodorderingapp.Fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderingapp.R
import com.example.foodorderingapp.adapter.BuyAgainAdapter
import com.example.foodorderingapp.databinding.FragmentHistoryBinding
import com.example.foodorderingapp.model.OrderDetailsModel
import com.example.foodorderingapp.setSafeOnClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetailsModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        retrieveBuyHistory()
        binding.cardViewItem.setSafeOnClickListener {
            setItemsRecentBuy()
        }
        binding.btnReceived.setSafeOnClickListener {
            updateOrderStatus()
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun setItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val itemCount = recentBuy.foodName?.size ?: 0
            android.util.Log.d("RecentOrder", "Recent buy te total item: $itemCount")
            val intent = Intent(requireContext(), RecentOrderViewItemsActivity::class.java)
            intent.putExtra("RecentBuyOrderItems", ArrayList(listOfOrderItem))
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
        binding.cardViewItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""
        val buyItemReference = database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")
        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapShot in snapshot.children) {
                    val buyHistoryItem = buySnapShot.getValue(OrderDetailsModel::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecycleBuyItem()
                    setPreviousBuyItemsRecycleView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataInRecycleBuyItem() {
        binding.cardViewItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding) {
                tvFoodItemName.text = it.foodName?.firstOrNull() ?: ""
                tvFoodPriceCart.text = it.foodPrice?.firstOrNull() ?: ""
                val image = it.foodImage?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(iVFoodImage)
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setPreviousBuyItemsRecycleView()
                }
                val isOrderAccepted = listOfOrderItem[0].orderAccepted
                if(isOrderAccepted){
                    cvGreen.background.setTint(Color.GREEN)
                    btnReceived.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setPreviousBuyItemsRecycleView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImg = mutableListOf<String>()
        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodName?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            listOfOrderItem[i].foodPrice?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            listOfOrderItem[i].foodImage?.firstOrNull()?.let { buyAgainFoodImg.add(it) }
        }
        val rv = binding.rvPreviousItem
        rv.layoutManager = LinearLayoutManager(requireContext())
        buyAgainAdapter =
            BuyAgainAdapter(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImg, requireContext())
        rv.adapter = buyAgainAdapter
    }

}