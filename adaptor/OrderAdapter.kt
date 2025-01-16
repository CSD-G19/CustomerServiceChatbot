package com.project.genassist_ecommerce.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.project.genassist_ecommerce.databinding.OrderLayoutBinding
import com.project.genassist_ecommerce.model.OrderId
import com.project.genassist_ecommerce.utils.spanned




class OrderAdapter(
    private var orders: List<OrderId>,
    private val itemOnClick: (OrderId) -> Unit,
    private val statusOnClick: (OrderId) -> Unit,
    val status:String
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: OrderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val orderIdTextView = binding.orderId
        val btnAccept = binding.btnClickON
        val time = binding.time
        val paymentInfo = binding.paymentInfo

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = OrderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderIdTextView.text = spanned("<b>Order Id:</b> ${order.orderid}")
        holder.time.text = spanned("<b>Time:</b> ${order.date}")

        when (status) {
            "Pending" -> {
                holder.btnAccept.text = "Approve Order"
                holder.btnAccept.visibility = View.VISIBLE
                // holder.statusTextView.visibility = View.GONE
            }

            "Accepted" -> {
                holder.btnAccept.text = "Close Order"
                holder.btnAccept.visibility = View.VISIBLE
                // holder.statusTextView.visibility = View.GONE
            }

            "Completed" -> {
                holder.btnAccept.visibility = View.GONE
                //  holder.statusTextView.visibility = View.VISIBLE
            }

            else -> {
                holder.btnAccept.visibility = View.GONE
                // holder.statusTextView.visibility = View.GONE
            }
        }

        holder.binding.root.setOnClickListener {
            itemOnClick(order)
        }

        holder.btnAccept.setOnClickListener {
            statusOnClick(order)
        }


    }

    override fun getItemCount(): Int = orders.size


    fun submitOrders(list22: List<OrderId>) {
        orders = list22
        notifyDataSetChanged()
    }
}


