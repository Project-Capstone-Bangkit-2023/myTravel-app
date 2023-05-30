package com.capstoneproject.mytravel.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.mytravel.R


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}