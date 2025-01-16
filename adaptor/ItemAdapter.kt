package com.project.genassist_ecommerce.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.genassist_ecommerce.databinding.ViewCartBinding
import com.project.genassist_ecommerce.model.Order
import com.project.genassist_ecommerce.utils.CartManager

class ItemAdapter(private var context: Context, private var list: ArrayList<Order>) :
    RecyclerView.Adapter<ItemAdapter.IteMList>() {

    class IteMList(val binding: ViewCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IteMList {
        val view = ViewCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IteMList(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: IteMList, position: Int) {
        val item = list[position]
        holder.binding.apply {
            Glide.with(context).load(item.itemphoto).into(ivProduct)
            etName.text = item.itemname
            etPrice.text = CartManager.formatRupees((item.price).toDouble())
            etQuantity.text = "Qty: ${item.qty}"
        }
    }

}