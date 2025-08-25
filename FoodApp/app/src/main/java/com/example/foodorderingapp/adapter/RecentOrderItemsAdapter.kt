package com.example.foodorderingapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.RecentOrderBuyingItemBinding

class RecentOrderItemsAdapter(
    private var context: Context,
    private var foodNames: ArrayList<String>,
    private var foodImages: ArrayList<String>,
    private var foodPrice: ArrayList<String>,
    private var quantity: ArrayList<Int>,
) : RecyclerView.Adapter<RecentOrderItemsAdapter.RecentOrderViewHolder>() {

    inner class RecentOrderViewHolder(private val binding: RecentOrderBuyingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvFoodName.text = foodNames[position]
                tvPrice.text = foodPrice[position]
                tvQuantityNum.text = quantity[position].toString()
                val uriString = foodImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartFoodImg)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentOrderItemsAdapter.RecentOrderViewHolder {
        val binding =
            RecentOrderBuyingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentOrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecentOrderItemsAdapter.RecentOrderViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNames.size
}