package com.example.foodorderingapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentUserBinding
import com.example.foodorderingapp.model.UserModel
import com.example.foodorderingapp.setSafeOnClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setUserData()

        binding.etName.isEnabled = false
        binding.etEmail.isEnabled = false
        binding.etAddress.isEnabled = false
        binding.etPhone.isEnabled = false

        var isEnable = false

        binding.btnEditYourProfile.setSafeOnClickListener {
            isEnable = !isEnable

            binding.etName.isEnabled = isEnable
            binding.etEmail.isEnabled = isEnable
            binding.etAddress.isEnabled = isEnable
            binding.etPhone.isEnabled = isEnable

            if (isEnable) {
                binding.etName.requestFocus()
            }
        }

        binding.btSaveInfor.setSafeOnClickListener {
            val name = binding.etName.text.toString()
            val address = binding.etAddress.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            updateUserData(name, address, email, phone)
        }
        return binding.root
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)
            val userData = hashMapOf(
                "userPerson" to name,
                "address" to address,
                "usEmail" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile update successfully", Toast.LENGTH_LONG)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile update failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.etName.setText(userProfile.userPerson)
                            binding.etAddress.setText(userProfile.address)
                            binding.etEmail.setText(userProfile.usEmail)
                            binding.etPhone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

}