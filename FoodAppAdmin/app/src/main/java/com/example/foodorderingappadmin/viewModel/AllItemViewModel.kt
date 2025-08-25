package com.example.foodorderingappadmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodorderingappadmin.model.AllMenu
import com.google.firebase.database.*

class AllItemViewModel : ViewModel() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("menu")

    private val _menuItems = MutableLiveData<List<AllMenu>>()
    val menuItems: LiveData<List<AllMenu>> get() = _menuItems

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    // -------------------------------
    // 🔹 Retrieve all menu items
    // -------------------------------
    fun retrieveMenuItems() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<AllMenu>()
                for (child in snapshot.children) {
                    val menuItem = child.getValue(AllMenu::class.java)
                    menuItem?.let {
                        it.key = child.key ?: ""
                        list.add(it)
                    }
                }
                list.reverse()
                _menuItems.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                _message.value = error.message
            }
        })
    }

    // -------------------------------
    // 🔹 Delete menu item
    // -------------------------------
    fun deleteMenuItem(menuItem: AllMenu) {
        if (menuItem.key.isEmpty()) {
            _message.value = "Item key not found"
            return
        }

        database.child(menuItem.key).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _message.value = "Item deleted successfully"
                // Refresh list
                retrieveMenuItems()
            } else {
                _message.value = "Failed to delete: ${task.exception?.message}"
            }
        }
    }
}
