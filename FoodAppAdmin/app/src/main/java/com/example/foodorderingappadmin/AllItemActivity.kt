package com.example.foodorderingappadmin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingappadmin.adapter.AllItemAdapter
import com.example.foodorderingappadmin.databinding.ActivityAllItemBinding
import com.example.foodorderingappadmin.model.AllMenu
import com.example.foodorderingappadmin.viewModel.AllItemViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllItemBinding
    private lateinit var viewModel: AllItemViewModel
    private lateinit var adapter: AllItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AllItemViewModel::class.java]
        adapter = AllItemAdapter(this, mutableListOf())
        binding.menuRecycleView.layoutManager = LinearLayoutManager(this)
        binding.menuRecycleView.adapter = adapter

        viewModel.menuItems.observe(this) { items ->
            adapter.updateData(items)
        }

        viewModel.message.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        adapter.setOnDeleteClickListener { menuItem ->
            viewModel.deleteMenuItem(menuItem)
        }

        viewModel.retrieveMenuItems()

        binding.btnBack.setSafeOnClickListener { finish() }

    }

}

/*
private lateinit var binding: ActivityAllItemBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllMenu> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItems()

        binding.btnBack.setSafeOnClickListener {
            finish()
        }
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for (foodSnapShot: DataSnapshot in snapshot.children) {
                    val menuItem: AllMenu? = foodSnapShot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        it.key = foodSnapShot.key ?: ""
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error: ${error.message}")
            }

        })
    }

    private fun setAdapter() {
        val adapter =
            AllItemAdapter(this@AllItemActivity, menuItems, databaseReference)
        binding.menuRecycleView.layoutManager = LinearLayoutManager(this)
        binding.menuRecycleView.adapter = adapter
    }
 */