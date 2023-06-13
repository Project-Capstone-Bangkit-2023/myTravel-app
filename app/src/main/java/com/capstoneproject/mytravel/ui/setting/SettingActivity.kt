package com.capstoneproject.mytravel.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.MainActivity
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.databinding.ActivitySettingBinding
import com.capstoneproject.mytravel.model.UserPreference

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

        settingViewModel.getUser().observe(this){user ->
            Glide.with(this)
                .load(user.photo_url)
                .into(binding.profileImage)

            binding.tvName.text = user.name
            binding.tvEmail.text = user.email
            binding.tvAge.text = user.age.toString()
            binding.tvLocation.text = user.location
            binding.tvCatPref.text = user.cat_pref
        }

        binding.btnLogout.setOnClickListener{
            settingViewModel.logout()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.btnEdit.setOnClickListener{
            startActivity(Intent(this@SettingActivity, EditProfileActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}