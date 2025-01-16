package com.project.genassist_ecommerce.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.genassist_ecommerce.databinding.ActivityAdminDashboardBinding

import com.project.genassist_ecommerce.utils.SessionManager


class AdminDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityAdminDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.logout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    shared.clearLoginState()
                    finish()
                    startActivity(Intent(this@AdminDashboard,LoginActivity::class.java))
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        bind.addsellers.setOnClickListener {
            startActivity(Intent(this, AddSellerActivity::class.java))
        }


        bind.viewSeller.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra("Key", "Seller")
            })
        }



    }
}