package com.example.foodorderingappadmin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderingappadmin.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("user")

    // LiveData user data
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    // LiveData for errors / success messages
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    // -------------------------------
    // 🔹 Retrieve user data
    // -------------------------------
    fun retrieveUserData() {
        val currentUserId = auth.currentUser?.uid ?: return
        val userReference = database.child(currentUserId)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    userModel?.let { _user.value = it }
                } else {
                    _message.value = "User data not found"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _message.value = error.message
            }
        })
    }

    // -------------------------------
    // 🔹 Update user data
    // -------------------------------
    fun updateUserData(updatedUser: UserModel) {
        val currentUserId = auth.currentUser?.uid ?: run {
            _message.value = "User not logged in"
            return
        }

        val userReference = database.child(currentUserId)
        userReference.setValue(updatedUser).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update FirebaseAuth email/password
                auth.currentUser?.updateEmail(updatedUser.usEmail ?: "")
                auth.currentUser?.updatePassword(updatedUser.usPass ?: "")
                _user.value = updatedUser
                _message.value = "Profile Updated Successfully"
            } else {
                _message.value = "Profile Update Failed: ${task.exception?.message}"
            }
        }
    }

}