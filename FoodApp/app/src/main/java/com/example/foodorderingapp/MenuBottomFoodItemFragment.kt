package com.example.foodorderingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.CartAdapter
import com.example.foodorderingapp.adapter.MenuAdapter
import com.example.foodorderingapp.databinding.FragmentMenuBottomFoodItemBinding
import com.example.foodorderingapp.model.MenuModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBottomFoodItemFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomFoodItemBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomFoodItemBinding.inflate(inflater, container, false)
        binding.btnBack.setSafeOnClickListener {
            dismiss()
        }
        retrieveMenuItems()

        return binding.root

    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapShot: DataSnapshot in snapshot.children) {
                    val menuItem: MenuModel? = foodSnapShot.getValue(MenuModel::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("ITEMS", "onDataChange: Data Received")
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setAdapter() {
        if (menuItems.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.rvMenuItem.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMenuItem.adapter = adapter
            Log.d("ITEMS", "setAdapter: data set")
        } else {
            Log.d("ITEMS", "setAdapter: data not set")
        }
    }

}