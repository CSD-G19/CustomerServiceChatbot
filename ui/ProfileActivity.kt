package com.project.genassist_ecommerce.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.genassist_ecommerce.databinding.ActivityProfileBinding
import com.project.genassist_ecommerce.utils.SessionManager

class ProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProfileBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.userLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    shared.clearLoginState()
                    dialog.dismiss()
                    startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }


        binding.editTextName.text = "${shared.getUserName()}"
        binding.editTextAdress.text = "${shared.getUserLocation()}"
        binding.editTextEmail.text = "${shared.getUserEmail()}"
        binding.editTextMoblie.text = "${shared.getUserMobile()}"
        binding.editTextPassword.text = "${shared.getUserPassword()}"


    }
}