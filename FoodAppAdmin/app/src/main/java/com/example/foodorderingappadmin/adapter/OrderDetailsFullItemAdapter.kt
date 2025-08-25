package com.example.foodorderingappadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingappadmin.databinding.OrderDetailsItemBinding

class OrderDetailsFullItemAdapter(
    private var context: Context,
    private var foodName: ArrayList<String>,
    private var foodImage: ArrayList<String>,
    private var foodQuantity: ArrayList<Int>,
    private var foodPrice: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsFullItemAdapter.OrderDetailsFullItemViewHolder>() {

    inner class OrderDetailsFullItemViewHolder(private val binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvFoodName.text = foodName[position]
                tvQuantityNum.text = foodQuantity[position].toString()
                tvPrice.text = foodPrice[position]
                val uriString = foodImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartFoodImg)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsFullItemViewHolder {
        val binding =
            OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsFullItemViewHolder(binding)
    }

    override fun getItemCount(): Int = foodName.size

    override fun onBindViewHolder(holder: OrderDetailsFullItemViewHolder, position: Int) {
        holder.bind(position)
    }

}