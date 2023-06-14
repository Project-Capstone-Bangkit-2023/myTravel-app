package com.capstoneproject.mytravel.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.MainActivity
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.databinding.ActivitySettingBinding
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SettingViewModel::class.java]

        settingViewModel.isLoading.observe(this){
            showLoading(it)
        }

        settingViewModel.getUser().observe(this){user ->
            Glide.with(this)
                .load(user.photo_url)
                .into(binding.profileImage)

            val token = user.token
            val email = user.email

            settingViewModel.getProfile(token, email)
            settingViewModel.dataProfile.observe(this){
                binding.tvName.text = it.name
                binding.tvEmail.text = it.email
                val strAge = (((it.age.toString()).toDouble()).toInt()).toString()
                binding.tvAge.text = strAge
                binding.tvLocation.text = it.location
                binding.tvCatPref.text = it.catPref.toString()
            }
        }

        binding.btnLogout.setOnClickListener{
            settingViewModel.logout()
            val intent = Intent(this@SettingActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnEdit.setOnClickListener{
            startActivity(Intent(this@SettingActivity, EditProfileActivity::class.java))
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            startActivity(Intent(this@SettingActivity, MainActivity::class.java))
            isEnabled = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}