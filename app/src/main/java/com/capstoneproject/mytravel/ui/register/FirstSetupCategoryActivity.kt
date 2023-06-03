package com.capstoneproject.mytravel.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.mytravel.databinding.ActivityFirstSetupCategoryBinding
import com.capstoneproject.mytravel.model.UserModel

class FirstSetupCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstSetupCategoryBinding
    private lateinit var firstSetupViewModel: FirstSetupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstSetupCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<UserModel>("DATA")
        val name = data?.name.toString()
        val email = data?.email.toString()
        val photoUrl = data?.photo_url.toString()
        val sendLoc = data?.location.toString()
        val tokenBearer = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiUmhlc2EgRGV2YXJhIiwiZW1haWwiOiJyaGVzYWRldmFyYXdAZ21haWwuY29tIiwibG9jYXRpb24iOiJCZWthc2ksIEphd2EgQmFyYXQiLCJhZ2UiOjIyLCJjYXRfcHJlZiI6IkJhaGFyaSIsImlhdCI6MTBZkWWuUHNahSjQZtmeoQYjMvmHe1WYuCT0fQ.aRLqNYOQwylEPDTemmglnNYec-ZnoksyTNeSDJhwKBU"
        
    }


}