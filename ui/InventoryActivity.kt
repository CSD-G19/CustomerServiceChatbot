package com.project.genassist_ecommerce.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.genassist_ecommerce.databinding.ActivityInventoryBinding
import com.project.genassist_ecommerce.response.ProductResponse
import com.project.genassist_ecommerce.response.RetrofitInstance
import com.project.genassist_ecommerce.utils.SessionManager
import com.project.genassist_ecommerce.utils.showToast
import com.project.genassist_ecommerce.adapter.ItemAdapter2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryActivity : AppCompatActivity() {
    private val bind by lazy { ActivityInventoryBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }
    private lateinit var itemAdapter: ItemAdapter2
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        id = if (intent.getStringExtra("role") == "Seller"){
            intent.getStringExtra("key").toString()
        }else{
            shared.getUserId()!!
        }


        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()

        CoroutineScope(IO).launch {
            bind.progressBar2.isVisible = true
            RetrofitInstance.instance.getProductsById(id)
                .enqueue(object : Callback<ProductResponse?> {
                    override fun onResponse(
                        p0: Call<ProductResponse?>,
                        p1: Response<ProductResponse?>
                    ) {
                        val response = p1.body()!!

                        Log.e("fjhdjfh", "onResponse: ${response.data}", )
                        if (response.error) {
                            showToast("List failed to load")
                            bind.progressBar2.isVisible = false

                        } else {
                            val list = response.data!!
                            itemAdapter = ItemAdapter2(this@InventoryActivity, list)
                            bind.rvProductList.layoutManager = LinearLayoutManager(this@InventoryActivity)
                            bind.rvProductList.adapter = itemAdapter
                            showToast("Loaded")
                            bind.progressBar2.isVisible = false

                        }

                    }

                    override fun onFailure(p0: Call<ProductResponse?>, p1: Throwable) {
                        showToast(p1.message!!)
                    }
                })
        }

    }
}