package com.project.genassist_ecommerce.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.project.genassist_ecommerce.R
import com.project.genassist_ecommerce.databinding.ActivityLoginBinding
import com.project.genassist_ecommerce.response.CommonResponse
import com.project.genassist_ecommerce.response.RetrofitInstance
import com.project.genassist_ecommerce.utils.SessionManager
import com.project.genassist_ecommerce.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val bind by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)


        if (shared.isLoggedIn()) {
            shared.getUserRole()?.let { navigateToDashboard(it) }
        }

        bind.textviewnew.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        val roles = resources.getStringArray(R.array.roles)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bind.spinner.adapter = adapter

        bind.buttonLogin.setOnClickListener {
            val email = bind.editTextEmail.text.toString().trim()
            val password = bind.editTextPassword.text.toString().trim()
            val role = bind.spinner.selectedItem.toString()

            if (email.isEmpty()) {
                showToast("Please enter your email")
            } else if (password.isEmpty()) {
                showToast("Please enter your password")
            } else if (role.isEmpty()) {
                showToast("Please select a valid role")
            } else {
                if (email == "admin" && password == "admin" && role == "Admin") {
                    shared.saveLoginState("-1", "Admin", "", "", "", "", "", "")
                    navigateToDashboard(role)
                    finish()
                } else {
                    bind.progressBar.isVisible = true
                    CoroutineScope(IO).launch {
                        RetrofitInstance.instance.userLogin(email, password, role)
                            .enqueue(object : Callback<CommonResponse?> {
                                override fun onResponse(
                                    call: Call<CommonResponse?>,
                                    response: Response<CommonResponse?>
                                ) {
                                    val loginResponse = response.body()!!
                                    if (!loginResponse.error) {
                                        loginResponse.data.firstOrNull()?.let { user ->
                                            shared.saveLoginState(
                                                "${user.id}",
                                                role,
                                                user.name,
                                                user.location,
                                                user.mobile,
                                                user.email,
                                                user.password,
                                                user.rating
                                            )
                                            navigateToDashboard(role)

                                        }
                                    } else {
                                        showToast("Invalid credentials")
                                    }
                                    bind.progressBar.isVisible = false
                                }


                                override fun onFailure(call: Call<CommonResponse?>, t: Throwable) {
                                    showToast(t.message ?: "Login failed")
                                    bind.progressBar.isVisible = false
                                }
                            })
                    }

                }
            }
        }
    }


    private fun navigateToDashboard(role: String) {
        val intent = when (role) {
            "Seller" -> Intent(this, SellerDashboard::class.java)
            "User" -> Intent(this, UserDashboard::class.java)
            else -> Intent(this, AdminDashboard::class.java)
        }
        startActivity(intent)
        finish()
    }

    }
