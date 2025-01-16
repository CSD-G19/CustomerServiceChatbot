package com.project.genassist_ecommerce.model


data class Products(
    var id: Int,
    var itemName: String,
    var itemPhoto: String,
    var price:Int,
    var sellerId:String?,
    var shopName:String?,
    var rating:String?
)