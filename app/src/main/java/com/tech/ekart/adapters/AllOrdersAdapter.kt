package com.tech.ekart.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tech.ekart.R
import com.tech.ekart.data.order.Order
import com.tech.ekart.data.order.OrderStatus
import com.tech.ekart.data.order.getOrderStatus
import com.tech.ekart.databinding.OrderItemBinding

class AllOrdersAdapter : RecyclerView.Adapter<AllOrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(private val binding : OrderItemBinding) : ViewHolder(binding
        .root){
        fun bind(order : Order){
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date

                val colorDrawable = when(getOrderStatus(order.orderStatus)){
                    is OrderStatus.Ordered->{
                        ColorDrawable(ContextCompat.getColor(root.context,R.color.g_orange_yellow))
                    }
                    is OrderStatus.Confirmed->{
                        ColorDrawable(ContextCompat.getColor(root.context,R.color.g_green))
                    }
                    is OrderStatus.Delivered->{
                        ColorDrawable(ContextCompat.getColor(root.context,R.color.g_green))
                    }
                    is OrderStatus.Shipped->{
                        ColorDrawable(ContextCompat.getColor(root.context,R.color.g_green))
                    }
                    is OrderStatus.Canceled->{
                        ColorDrawable(ContextCompat.getColor(root.context,R.color.g_red))
                    }
                    is OrderStatus.Returned->{
                        ColorDrawable(ContextCompat.getColor(root.context,R.color.g_red))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }
    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllOrdersAdapter.OrdersViewHolder {
        return OrdersViewHolder(
            OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: AllOrdersAdapter.OrdersViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var onClick : ((Order) -> Unit) ?= null
}