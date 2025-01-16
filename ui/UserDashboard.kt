package com.project.genassist_ecommerce.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.genassist_ecommerce.databinding.ActivityUserDashboardBinding
import com.project.genassist_ecommerce.response.ProductResponse
import com.project.genassist_ecommerce.response.RetrofitInstance
import com.project.genassist_ecommerce.utils.CartManager
import com.project.genassist_ecommerce.utils.SessionManager
import com.project.genassist_ecommerce.utils.showToast
import com.project.genassist_ecommerce.adapter.ProductAdapter
import com.project.genassist_ecommerce.chatbot.ChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityUserDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        CartManager.clearCart()

        bind.chat.setOnClickListener {
            startActivity(Intent(this@UserDashboard, ChatActivity::class.java))
        }

        bind.usetTxt.text = "Welcome\n ${shared.getUserName()} 😀"

        bind.profile.setOnClickListener {
            startActivity(Intent(this@UserDashboard, ProfileActivity::class.java))
        }

        bind.orders.setOnClickListener {
            startActivity(Intent(this, ViewUserOrderActivity::class.java))
        }

        bind.cart.setOnClickListener {
            if (CartManager.getTotalQuantity() < 1) {
                showToast("Empty Cart")
            } else {
                val intent = Intent(applicationContext, CartActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        CoroutineScope(IO).launch {
            RetrofitInstance.instance.getAllProducts().enqueue(object : Callback<ProductResponse?> {
                override fun onResponse(
                    p0: Call<ProductResponse?>,
                    p1: Response<ProductResponse?>
                ) {
                    val response = p1.body()!!
                    val products = response.data!!
                    if (response.error) {
                        showToast(response.message)
                    } else {
                        Log.d("dfhdjkhdsf", "onResponse: $products")
                        if (products.isEmpty()) {
                            showToast("No Records")
                        } else {
                            val productAdapter = ProductAdapter(
                                applicationContext, products
                            )
                            bind.rvzl.adapter = productAdapter
                            bind.rvzl.layoutManager = LinearLayoutManager(this@UserDashboard)


                        }
                    }
                    bind.progressBar3.isVisible = false
                }

                override fun onFailure(p0: Call<ProductResponse?>, p1: Throwable) {
                    showToast(p1.message!!)
                }
            })
        }


    }


}
