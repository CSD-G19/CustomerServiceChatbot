package com.project.genassist_ecommerce.response

import com.project.genassist_ecommerce.model.ChatRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("Entries.php")
    fun userRegister(
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("location") location: String,
        @Field("email") email: String,
        @Field("role") role: String,
        @Field("rating") farmRating:String
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("Login.php")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("Login.php")
    fun getuser(): Call<CommonResponse>

    @FormUrlEncoded
    @POST("GetEntries.php")
    fun getRole(
        @Field("role") role: String
    ): Call<CommonResponse>

    @GET("GetAllProducts.php")
    fun getAllProducts(): Call<ProductResponse>

    @FormUrlEncoded
    @POST("GetProductsById.php")
    fun getProductsById(
        @Field("sellerId") id: String
    ): Call<ProductResponse>

    @FormUrlEncoded
    @POST("GetOrdersByOrderId.php")
    fun getOrdersById(
        @Field("orderid") orderid: String,
    ): Call<ProductResponse>

    @FormUrlEncoded
    @POST("GetOrdersBySellerId.php")
    fun getOrdersBySellerId(
        @Field("sellerid") sellerid: String,
    ): Call<ProductResponse>

    @FormUrlEncoded
    @POST("InsertOrderById.php")
    fun placeTheOrder(
        @Field("orderid") orderid: String,
        @Field("userid") userid: String,
        @Field("sellerid") sellerid: String,
        @Field("status") status: String,
        @Field("itemphoto") itemphoto: String,
        @Field("itemname") itemname: String,
        @Field("qty") qty: String,
        @Field("price") price: String,
        @Field("date") date: String
    ):Call<ProductResponse>

    @FormUrlEncoded
    @POST("GetOrdersByIdStatus.php")
    fun getOrdersByIdStatus(
        @Field("status") status:String,
        @Field("userid") id: String,
        @Field("sellerid") sellerid:String
    ): Call<ProductResponse>


    @FormUrlEncoded
    @POST("UpdateOrderStatus.php")
    fun updateOrderStatus(
        @Field("status") status:String,
        @Field("orderid") orderid: String,
        @Field("userid") userid: String,
        @Field("sellerid") sellerid:String
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("AddItems.php")
    fun AddItem(
        @Field("itemName") itemName: String,
        @Field("itemPhoto") itemPhoto: String,
        @Field("price") price: String,
        @Field("sellerId") sellerId:String,
        @Field("shopName") shopName:String,
        @Field("rating") rating:String
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("DistinctOrderId.php")
    fun getOrderDetails(
        @Field("userid") userid: String,
        @Field("sellerid") sellerid:String,
        @Field("status") status: String
    ): Call<ProductResponse>

    @FormUrlEncoded
    @POST("GetOrderIdByUserId.php")
    fun getOrderDetailsByUser(
        @Field("userid") userid: String,
    ): Call<ProductResponse>


    @POST("v1/completions")
    fun getChatResponse(
        @Header("Authorization") apiKey: String,
        @Body requestBody: ChatRequest
    ): Call<ChatResponse>


}