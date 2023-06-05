package com.capstoneproject.mytravel.ui.register

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.mytravel.MainActivity
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.databinding.ActivityFirstSetupBinding
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class FirstSetupActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var binding: ActivityFirstSetupBinding
    private lateinit var firstSetupViewModel: FirstSetupViewModel
    private lateinit var location: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        getMyLastLocation()

        setupViewModel()
        val data = intent.getParcelableExtra<UserModel>("DATA")
        val name = data?.name.toString()
        val email = data?.email.toString()
        val photoUrl = data?.photo_url.toString()

        binding.btnNext.setOnClickListener(){
            val sendLoc = binding.locationEditText.text.toString()
            val ageStr = binding.ageEditText.text.toString()
            val age = ageStr.toInt()
            val dataFirstSetup =
                UserModel(0, photoUrl, name, email, sendLoc, age, "", false, "")
            val intentToDetail =
                Intent(this@FirstSetupActivity, FirstSetupCategoryActivity::class.java)
            intentToDetail.putExtra("DATA", dataFirstSetup)
            startActivity(intentToDetail)
        }
    }

    private fun setupViewModel() {
        firstSetupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[FirstSetupViewModel::class.java]

        firstSetupViewModel.isLoading.observe(this) { showLoading(it) }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {

                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if  (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    EXTRA_LAT = lat
                    EXTRA_LON = lon

                    val address = geocoder.getFromLocation(lat,lon,1)
                    val city = address!![0].subAdminArea.toString()
                    val thoroughFare = address[0].thoroughfare.toString()
                    val displayAddress = "$city , $thoroughFare"
                    this@FirstSetupActivity.location = displayAddress
                    EXTRA_LOCATION = displayAddress

                    binding.locationEditText.setText(displayAddress)
                    println(address)
                    binding.locationEditText.setText(address[0].getAddressLine(0).toString())
                } else {
                    Toast.makeText(
                        this@FirstSetupActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object{
        var EXTRA_LAT = 0.0
        var EXTRA_LON = 0.0
        var EXTRA_LOCATION = ""
    }

}

