package com.example.foodorderingappadmin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.foodorderingappadmin.databinding.ActivityAdminProfileBinding
import com.example.foodorderingappadmin.model.UserModel
import com.example.foodorderingappadmin.viewModel.AdminProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminProfileBinding
    private lateinit var viewModel: AdminProfileViewModel
    private var isEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AdminProfileViewModel::class.java]
        binding.btnBack.setSafeOnClickListener { finish() }
        disableFields()

        binding.tvClickHereToEdit.setSafeOnClickListener {
            isEnable = !isEnable
            setFieldsEnabled(isEnable)
            if (isEnable) binding.etName.requestFocus()
        }

        binding.btnSaveInformation.setSafeOnClickListener {
            val updatedUser = UserModel(
                userPerson = binding.etName.text.toString(),
                usEmail = binding.etEmail.text.toString(),
                usPass = binding.etPassword.text.toString(),
                address = binding.etAddress.text.toString(),
                phone = binding.etPhone.text.toString()
            )
            viewModel.updateUserData(updatedUser)
        }

        viewModel.user.observe(this) { user ->
            binding.etName.setText(user.userPerson)
            binding.etEmail.setText(user.usEmail)
            binding.etPassword.setText(user.usPass)
            binding.etAddress.setText(user.address)
            binding.etPhone.setText(user.phone)
        }

        viewModel.message.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }

        viewModel.retrieveUserData()

    }

    private fun setFieldsEnabled(enabled: Boolean) {
        binding.etName.isEnabled = enabled
        binding.etEmail.isEnabled = enabled
        binding.etPassword.isEnabled = enabled
        binding.etAddress.isEnabled = enabled
        binding.etPhone.isEnabled = enabled
        binding.btnSaveInformation.isEnabled = enabled
    }

    private fun disableFields() {
        setFieldsEnabled(false)
    }
}

/*
private lateinit var binding: ActivityAdminProfileBinding
private lateinit var database: FirebaseDatabase
private lateinit var auth: FirebaseAuth
private lateinit var adminProfileReference: DatabaseReference

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAdminProfileBinding.inflate(layoutInflater)
    setContentView(binding.root)

    auth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance()
    adminProfileReference = database.reference.child("user")
    binding.btnBack.setSafeOnClickListener {
        finish()
    }
    binding.btnSaveInformation.setSafeOnClickListener {
        updateUserData()
    }

    binding.etName.isEnabled = false
    binding.etEmail.isEnabled = false
    binding.etAddress.isEnabled = false
    binding.etPhone.isEnabled = false
    binding.etPassword.isEnabled = false
    binding.btnSaveInformation.isEnabled = false

    var isEnable = false

    binding.tvClickHereToEdit.setSafeOnClickListener {
        isEnable =! isEnable
        binding.etName.isEnabled = isEnable
        binding.etEmail.isEnabled = isEnable
        binding.etAddress.isEnabled = isEnable
        binding.etPhone.isEnabled = isEnable
        binding.etPassword.isEnabled = isEnable
        if (isEnable) {
            binding.etName.requestFocus()
        }
        binding.btnSaveInformation.isEnabled = isEnable
    }
    retrieveUserData()
}

private fun retrieveUserData() {
    val currentUserId = auth.currentUser?.uid
    if (currentUserId != null) {
        val userReference = adminProfileReference.child(currentUserId)
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var ownerName = snapshot.child("userPerson").getValue()
                    var email = snapshot.child("usEmail").getValue()
                    var password = snapshot.child("usPass").getValue()
                    var address = snapshot.child("address").getValue()
                    var phone = snapshot.child("phone").getValue()
                    setDataToTextview(ownerName, email, password, address, phone)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

private fun setDataToTextview(
    ownerName: Any?,
    email: Any?,
    password: Any?,
    address: Any?,
    phone: Any?
) {
    binding.etName.setText(ownerName.toString())
    binding.etEmail.setText(email.toString())
    binding.etPassword.setText(password.toString())
    binding.etAddress.setText(address.toString())
    binding.etPhone.setText(phone.toString())
}

private fun updateUserData() {
    var updateName = binding.etName.text.toString()
    var updateEmail = binding.etEmail.text.toString()
    var updatePassword = binding.etPassword.text.toString()
    var updateAddress = binding.etAddress.text.toString()
    var updatePhone = binding.etPhone.text.toString()
    val currentUserId = auth.currentUser?.uid
    if (currentUserId != null) {
        val userReference = adminProfileReference.child(currentUserId)
        userReference.child("userPerson").setValue(updateName)
        userReference.child("usEmail").setValue(updateEmail)
        userReference.child("usPass").setValue(updatePassword)
        userReference.child("address").setValue(updateAddress)
        userReference.child("phone").setValue(updatePhone)
        Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_LONG).show()
        auth.currentUser?.updateEmail(updateEmail)
        auth.currentUser?.updatePassword(updatePassword)
    }
    else{
        Toast.makeText(this, "Profile Updated Failed", Toast.LENGTH_LONG).show()
    }
}
 */