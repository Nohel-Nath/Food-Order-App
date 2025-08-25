package com.example.foodorderingapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telecom.Call.Details
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.FoodDetailsActivity
import com.example.foodorderingapp.databinding.MenuItemBinding
import com.example.foodorderingapp.model.MenuModel
import com.example.foodorderingapp.setSafeOnClickListener


class MenuAdapter(
    private val menuItems: List<MenuModel>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuHolder>() {

    private val itemClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
        return MenuHolder(
            MenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    inner class MenuHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setSafeOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                }
            }
        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                tvFoodItemMenu.text = menuItem.foodName
                tvFoodPriceMenu.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(imageViewFoodImgMenu)
            }
        }
    }

    private fun openDetailsActivity(position: Int) {
        val menuItem = menuItems[position]
        val intent = Intent(requireContext, FoodDetailsActivity::class.java).apply {
            putExtra("MenuItemName", menuItem.foodName)
            putExtra("MenuItemImage", menuItem.foodImage)
            putExtra("MenuItemDescription", menuItem.foodDescription)
            putExtra("MenuItemIngredients", menuItem.foodIngredients)
            putExtra("MenuItemPrice", menuItem.foodPrice)
        }
        requireContext.startActivity(intent)
    }

}

