package com.project.genassist_ecommerce.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.genassist_ecommerce.databinding.ActivityViewUserOrderBinding
import com.project.genassist_ecommerce.response.ProductResponse
import com.project.genassist_ecommerce.response.RetrofitInstance
import com.project.genassist_ecommerce.utils.SessionManager
import com.project.genassist_ecommerce.utils.showToast
import com.project.genassist_ecommerce.adapter.OrderAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewUserOrderActivity : AppCompatActivity() {
    private val bind by lazy { ActivityViewUserOrderBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    private lateinit var orderAdapter: OrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        val id = shared.getUserId()!!

        orderAdapter = OrderAdapter(emptyList(), {
            startActivity(Intent(this, ProductsActivity::class.java).apply {
                putExtra("jet", it.orderid)
            })
        }, {

        }, "")

        bind.rvList.layoutManager = LinearLayoutManager(this)
        bind.rvList.adapter = orderAdapter

        CoroutineScope(IO).launch {
            RetrofitInstance.instance.getOrderDetailsByUser("$id")
                .enqueue(object : Callback<ProductResponse?> {
                    override fun onResponse(
                        p0: Call<ProductResponse?>,
                        p1: Response<ProductResponse?>
                    ) {
                        val response = p1.body()!!
                        if (response.error) {
                            showToast("Error occurred")
                        } else {
                            response.data5?.let {
                                if (it.isEmpty()) {
                                    showToast("No Records")
                                } else {
                                    orderAdapter.submitOrders(it)
                                }

                            }
                        }
                    }

                    override fun onFailure(p0: Call<ProductResponse?>, p1: Throwable) {
                        showToast(p1.message!!)
                    }
                })
        }


    }
}