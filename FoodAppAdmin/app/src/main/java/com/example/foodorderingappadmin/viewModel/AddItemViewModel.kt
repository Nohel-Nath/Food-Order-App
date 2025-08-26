package com.example.foodorderingappadmin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderingappadmin.model.AllMenu
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddItemViewModel: ViewModel() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("menu")

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    fun uploadData(item: AllMenu) {
        val newItemKey = database.push().key
        if (newItemKey != null) {
            database.child(newItemKey).setValue(item)
                .addOnSuccessListener {
                    _uploadStatus.value = "success"
                }
                .addOnFailureListener { e ->
                    _uploadStatus.value = "failed: ${e.message}"
                }
        } else {
            _uploadStatus.value = "failed: key null"
        }
    }

}