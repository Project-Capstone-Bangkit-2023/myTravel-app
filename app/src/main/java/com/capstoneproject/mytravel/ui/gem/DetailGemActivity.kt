package com.capstoneproject.mytravel.ui.gem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.retrofit.*
import com.capstoneproject.mytravel.ui.home.HomeViewModel
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class DetailGemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_gem)

    }
}