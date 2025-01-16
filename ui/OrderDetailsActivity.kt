package com.project.genassist_ecommerce.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.genassist_ecommerce.databinding.ActivityOrderDetailsBinding
import com.project.genassist_ecommerce.response.CommonResponse
import com.project.genassist_ecommerce.response.RetrofitInstance
import com.project.genassist_ecommerce.utils.showToast
import com.project.genassist_ecommerce.adapter.ViewDetailsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsActivity : AppCompatActivity() {
    private val bind by lazy { ActivityOrderDetailsBinding.inflate(layoutInflater) }
    var id = ""
    private lateinit var viewDetailsAdapter: ViewDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)


        val intent = intent.getStringExtra("order")!!

        viewDetailsAdapter = ViewDetailsAdapter(emptyList()) {
            id = "${it.id}"
            startActivity(Intent(this@OrderDetailsActivity, DistinctOrders::class.java).apply {
                putExtra("id", id)
                putExtra("status", intent)
            })
        }

        bind.rvListy.adapter = viewDetailsAdapter
        bind.rvListy.layoutManager = LinearLayoutManager(this)


        RetrofitInstance.instance.getRole("User").enqueue(object : Callback<CommonResponse?> {
            override fun onResponse(p0: Call<CommonResponse?>, p1: Response<CommonResponse?>) {
                val response = p1.body()!!
                if (response.error){
                    showToast("Error Occurred")
                }else{
                    viewDetailsAdapter.submitList(response.data)
                    Log.d("jshfjkdfh", "onResponse: ${response.data}")
                }
            }

            override fun onFailure(p0: Call<CommonResponse?>, p1: Throwable) {
                showToast(p1.message!!)
            }
        })


    }
}