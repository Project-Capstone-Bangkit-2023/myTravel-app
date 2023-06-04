package com.capstoneproject.mytravel.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.mytravel.HomeActivity
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.databinding.ActivityFirstSetupCategoryBinding
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
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

        firstSetupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[FirstSetupViewModel::class.java]

        binding.btnFinish.setOnClickListener{
            firstSetupViewModel.register(photoUrl,name,email,sendLoc,"Cagar Alam", tokenBearer)
            startActivity(Intent(this@FirstSetupCategoryActivity, HomeActivity::class.java))
            finish()
        }
    }

}