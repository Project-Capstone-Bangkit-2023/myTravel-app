package com.capstoneproject.mytravel.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.mytravel.BuildConfig
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.databinding.FragmentHomeBinding
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.WeatherResponse
import com.capstoneproject.mytravel.retrofit.WeatherService
import com.capstoneproject.mytravel.ui.setting.SettingActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.util.*


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireActivity(), Locale.getDefault())
        getLocation()

        binding.btnSetting.setOnClickListener{
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }

        setupViewModel()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[HomeViewModel::class.java]


        homeViewModel.getUser().observe(requireActivity()) { user ->
            println("INI TOKEN pada home: " + user.token)
            println("INI IS LOGIN pada home: " + user.isLogin)
        }

    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getLocation()
                }
                else -> {

                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        if  (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    println("LAT LOC : $lat")
                    println("LON LOC : $lon")

                    getTemperature(lat, lon)

                    val address = geocoder.getFromLocation(lat,lon,1)
                    val city = address!![0].subAdminArea.toString()
                    val thoroughFare = address[0].thoroughfare.toString()
                    val displayAddress = "$city , $thoroughFare"
                    binding.tvLocation.text = displayAddress
                } else {
                    Toast.makeText(
                        requireActivity(),
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

    private fun getTemperature(lat: Double, lon: Double){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        val call = weatherService.getCurrentWeather(lat, lon, BuildConfig.APP_ID)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val latWeather = it.coord.lat
                        val lonWeather = it.coord.lon
                        println("LAT WEATHER : $latWeather")
                        println("LON WEATHER : $lonWeather")
                        val temperatureKelvin = it.main.temp.toString()
                        val temperatureCelsius = temperatureKelvin.toDouble() - 273.0
                        val temperature = DecimalFormat("##.##").format(temperatureCelsius)
                        val humidity = it.main.humidity
                        binding.tvTemperature.text = resources.getString(R.string.temperature, temperature)
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

            }
        })
    }

}