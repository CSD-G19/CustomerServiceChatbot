package com.project.genassist_ecommerce.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.genassist_ecommerce.databinding.ActivityCartBinding
import com.project.genassist_ecommerce.response.ProductResponse
import com.project.genassist_ecommerce.response.RetrofitInstance
import com.project.genassist_ecommerce.utils.CartManager
import com.project.genassist_ecommerce.utils.SessionManager
import com.project.genassist_ecommerce.utils.showToast
import com.project.genassist_ecommerce.adapter.CartAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CartActivity : AppCompatActivity() {
    private val bind by lazy { ActivityCartBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    val simple = SimpleDateFormat("dd/MMMM/yyyy(hh:mm:ss)", Locale.getDefault())
    var price = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        val cartAdapter = CartAdapter(applicationContext, CartManager.getCartItems())
        Log.d("jkfdhjkdhf", "${CartManager.getCartItems()} ")
        bind.rvOrdersList.adapter = cartAdapter
        bind.rvOrdersList.layoutManager = LinearLayoutManager(this)

        price = CartManager.getFormattedPrice()
        bind.totalValue.text = price

        bind.btn1Place.setOnClickListener {
            val userId = "${shared.getUserId()}"
            val di = "LTHR${System.currentTimeMillis()}"
            CartManager.getCartItems().forEach { (products, quantity) ->
                Log.d("dhfjf", "onCreate: ${products.sellerId}")

                CoroutineScope(IO).launch {
                    RetrofitInstance.instance.placeTheOrder(
                        "$di",
                        "$userId",
                        sellerid = "${products.sellerId}",
                        status = "Pending",
                        itemphoto = products.itemPhoto,
                        itemname = products.itemName,
                        qty = "$quantity",
                        price = "${products.price * quantity}",
                        date = simple.format(Date()),
                    ).enqueue(object : Callback<ProductResponse?> {
                        override fun onResponse(
                            p0: Call<ProductResponse?>,
                            p1: Response<ProductResponse?>
                        ) {
                            val response = p1.body()!!
                            if (response.error) {
                                showToast("Failed")
                            } else {
                                showToast("Order Placed Successfully")
                                CartManager.clearCart()
                                finish()
                                startActivity(
                                    Intent(
                                        this@CartActivity,
                                        OrderPlaced::class.java
                                    ).putExtra("orderId", di)
                                )
                            }
                        }

                        override fun onFailure(p0: Call<ProductResponse?>, p1: Throwable) {
                            showToast(p1.message!!)
                        }
                    })
                }


            }
        }

    }
}