package com.project.genassist_ecommerce.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.genassist_ecommerce.databinding.ViewCartBinding
import com.project.genassist_ecommerce.model.Products
import com.project.genassist_ecommerce.utils.CartManager


class ItemAdapter2(private var context: Context, private var list: ArrayList<Products>) :
    RecyclerView.Adapter<ItemAdapter2.IteMList>() {

    class IteMList(val binding: ViewCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IteMList {
        val view = ViewCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IteMList(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IteMList, position: Int) {
        val item = list[position]

        holder.binding.apply {
            Glide.with(context).load(item.itemPhoto).into(holder.binding.ivProduct)

            etName.text = "${item.itemName}"
            etPrice.text = "${CartManager.formatRupees((item.price).toDouble())}"
            etQuantity.visibility = View.INVISIBLE
        }
    }

}