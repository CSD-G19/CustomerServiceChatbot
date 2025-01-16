package com.project.genassist_ecommerce.model


data class Order(
    var id:Int,
    var userid: Int,
    var productId: Int,
    var itemname: String,
    var qty: Int,
    var totalPrice: Int,
    val itemphoto: String,
    var status: String,
    var date: String,
    var price: String
)