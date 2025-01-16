package com.project.genassist_ecommerce.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.project.genassist_ecommerce.databinding.ActivityAddSellerBinding
import com.project.genassist_ecommerce.response.CommonResponse
import com.project.genassist_ecommerce.response.RetrofitInstance
import com.project.genassist_ecommerce.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSellerActivity : AppCompatActivity() {
    private val bind by lazy { ActivityAddSellerBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.button1.setOnClickListener {
            val shopName = bind.tiName.text.toString()
            val shopEmail = bind.tiEmail.text.toString()
            val shopPassword = bind.tiPassword.text.toString()
            val shopLocation = bind.location.text.toString()
            val shopMobile = bind.mobile.text.toString()
            val rating = bind.ratingBar.rating.toString()

            when {
                shopName.isEmpty() -> showToast("Please enter a valid name")
                shopEmail.isEmpty() || !shopEmail.contains("@gmail.com") -> showToast("Please enter a valid email")
                shopPassword.isEmpty() || shopPassword.length < 6 -> showToast("Please enter a valid password")
                shopLocation.isEmpty() -> showToast("Please enter a valid location")
                shopMobile.isEmpty() || shopMobile.length < 10 -> showToast("Please enter a valid mobile no.")
                rating.isEmpty() -> showToast("Please enter a valid entering")
                else -> {
                    bind.loadingBar.isVisible = true
                    bind.driverLayout.isVisible = false
                    CoroutineScope(IO).launch {
                        RetrofitInstance.instance.userRegister(
                            shopName,
                            shopMobile,
                            shopPassword,
                            shopLocation,
                            shopEmail,
                            "Seller",
                            rating
                        ).enqueue(object : Callback<CommonResponse?> {
                            override fun onResponse(
                                p0: Call<CommonResponse?>,
                                p1: Response<CommonResponse?>
                            ) {
                                if (p1.isSuccessful) {
                                    val response = p1.body()!!
                                    if (!response.error) {
                                        showToast("Data Inserted")
                                        finish()
                                    } else {
                                        showToast(response.message)
                                    }
                                } else {
                                    showToast("Response Failed")
                                }
                                bind.loadingBar.isVisible = false
                                bind.driverLayout.isVisible = true
                            }

                            override fun onFailure(p0: Call<CommonResponse?>, p1: Throwable) {
                                showToast(p1.message!!)
                                bind.loadingBar.isVisible = false
                                bind.driverLayout.isVisible = true
                            }
                        })
                    }

                }

            }
        }


    }
}