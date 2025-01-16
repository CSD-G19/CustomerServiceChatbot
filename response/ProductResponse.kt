package com.project.genassist_ecommerce.response

import com.project.genassist_ecommerce.model.Order
import com.project.genassist_ecommerce.model.OrderId
import com.project.genassist_ecommerce.model.Products


data class ProductResponse(
    var error: Boolean,
    var message: String,
    var data: ArrayList<Products>? = arrayListOf(),
    var data2: ArrayList<Order>? = arrayListOf(),
    var data5: ArrayList<OrderId>? = arrayListOf()
)