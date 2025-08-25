package com.example.foodorderingappadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingappadmin.databinding.AllItemBinding
import com.example.foodorderingappadmin.model.AllMenu
import com.example.foodorderingappadmin.setSafeOnClickListener
import com.google.firebase.database.DatabaseReference

class AllItemAdapter(
    private val context: Context,
    private val menuList: MutableList<AllMenu>,
) : RecyclerView.Adapter<AllItemAdapter.AllItemHolder>() {

    //private val itemQuantities = IntArray(menuList.size){1}
    private val itemQuantities = MutableList(menuList.size) { 1 }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllItemAdapter.AllItemHolder {
        val binding = AllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllItemHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemAdapter.AllItemHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AllItemHolder(private val binding: AllItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val menuItem = menuList[position]
            val quantity = itemQuantities[position]
            binding.apply {
                tvFoodItemCart.text = menuItem.foodName
                tvFoodPriceCart.text = menuItem.foodPrice
                tvQuantity.text = quantity.toString()
                Glide.with(context).load(Uri.parse(menuItem.foodImage)).into(cartFoodImg)
                btnInc.setSafeOnClickListener { increaseQuantity(position) }
                btnDec.setSafeOnClickListener { decreaseQuantity(position) }
                btnDelete.setSafeOnClickListener { onDeleteClickListener?.invoke(menuItem) }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 15) {
                itemQuantities[position]++
                binding.tvQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.tvQuantity.text = itemQuantities[position].toString()
            }
        }
    }

    fun updateData(newList: List<AllMenu>) {
        menuList.clear()
        menuList.addAll(newList)
        itemQuantities.clear()
        itemQuantities.addAll(MutableList(newList.size) { 1 })
        notifyDataSetChanged()
    }

    // -------------------------------
    // 🔹 Delete Callback
    // -------------------------------
    private var onDeleteClickListener: ((AllMenu) -> Unit)? = null
    fun setOnDeleteClickListener(listener: (AllMenu) -> Unit) {
        onDeleteClickListener = listener
    }

}