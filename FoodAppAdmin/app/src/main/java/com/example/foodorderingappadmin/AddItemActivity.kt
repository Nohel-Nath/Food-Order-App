package com.example.foodorderingappadmin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodorderingappadmin.databinding.ActivityAddItemBinding
import com.example.foodorderingappadmin.model.AllMenu
import com.example.foodorderingappadmin.viewModel.AddItemViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var viewModel: AddItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AddItemViewModel::class.java]

        // LiveData observe করা
        viewModel.uploadStatus.observe(this) { status ->
            when {
                status == "success" -> {
                    Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_LONG).show()
                    finish()
                }

                status.startsWith("failed") -> {
                    Toast.makeText(this, status, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.etSelectImage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val imageUriText = s.toString().trim()

                if (imageUriText.isNotEmpty()) {
                    try {
                        // Load image using Glide (for both URL/URI)
                        Glide.with(this@AddItemActivity)
                            .load(imageUriText)
                            .into(binding.ivItemImage)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@AddItemActivity,
                            "Invalid image URL",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        binding.btnAddItem.setSafeOnClickListener {
            val foodName = binding.etItemName.text.toString().trim()
            val foodPrice = binding.etItemPrice.text.toString().trim()
            val foodDes = binding.etDescription.text.toString().trim()
            val foodIngre = binding.etIngredients.text.toString().trim()
            val foodImageUrl = binding.etSelectImage.text.toString().trim()

            if (foodName.isNotBlank() && foodPrice.isNotBlank() && foodDes.isNotBlank() &&
                foodIngre.isNotBlank() && foodImageUrl.isNotBlank()
            ) {
                val newItem = AllMenu(
                    foodName = foodName,
                    foodPrice = foodPrice,
                    foodDescription = foodDes,
                    foodIngredients = foodIngre,
                    foodImage = foodImageUrl
                )
                viewModel.uploadData(newItem) // 🔹 ViewModel দিয়ে upload হচ্ছে
            } else {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnBack.setSafeOnClickListener {
            finish()
        }
    }
}

/*

    //            binding.tvSelectImage.setSafeOnClickListener {
//                pickImage.launch("image/*")
//            }


//        private val pickImage =
//            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//                if (uri != null) {
//                    binding.ivItemImage.setImageURI(uri)
//                    foodImg = uri
//                }
//            }

 */
    if (foodImg != null && newItemKey != null) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imgRef: StorageReference = storageRef.child("menu_images/${newItemKey}.jpg")
        val uploadTask: UploadTask = imgRef.putFile(foodImg!!)

        uploadTask.addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener { downloadUri ->
                val newItem = AllMenu(
                    foodName = foodName,
                    foodPrice = foodPrice,
                    foodDescription = foodDes,
                    foodIngredients = foodIngre,
                    foodImage = downloadUri.toString(),
                )

                menuRef.child(newItemKey).setValue(newItem)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Item Add Successfully", Toast.LENGTH_LONG).show()
                        finish() // Now only finish if data upload is successful
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data upload failed", Toast.LENGTH_LONG).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(this, "Please select an image", Toast.LENGTH_LONG).show()
    }

 */