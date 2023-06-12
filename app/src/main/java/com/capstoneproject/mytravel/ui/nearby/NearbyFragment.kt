package com.capstoneproject.mytravel.ui.nearby

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.adapter.Nearby
import com.capstoneproject.mytravel.adapter.NearbyAdapter
import com.capstoneproject.mytravel.databinding.FragmentNearbyBinding
import com.capstoneproject.mytravel.model.UserPreference
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class NearbyFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentNearbyBinding? = null
    private val binding get() = _binding
    private lateinit var nearbyViewModel: NearbyViewModel
    private lateinit var adapter: NearbyAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNearbyBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvStory?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvStory?.addItemDecoration(itemDecoration)

        getMyLastLocation()

        nearbyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[NearbyViewModel::class.java]


        nearbyViewModel.isLoading.observe(requireActivity()) { showLoading(it) }

    }
    private fun setPlaceData(placeData: List<Nearby>){
        val listPlace = ArrayList<Nearby>()
        for (i in placeData) {
            val id = i.id
            val name = i.name
            val category = i.category
            val photo = i.photo
            val desc = i.desc
            val city = i.city
            val strRating = i.rating.toString()
            val rating = strRating.toDouble()
            val lat = i.lat
            val lon = i.lon
            val price = i.price
            val distance = i.distance
            val place = Nearby(id,name, category, photo, city, rating, price, desc, lat, lon, distance)
            listPlace.add(place)
        }
        adapter = NearbyAdapter(listPlace)
        binding?.rvStory?.adapter = adapter
        adapter.setOnItemClickCallback(object : NearbyAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Nearby) {
                val intentToDetail = Intent(requireActivity(), DetailNearbyActivity::class.java)
                intentToDetail.putExtra("DATA", data)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
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
            requireContext(),
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

                    nearbyViewModel.getUser().observe(requireActivity()){ user ->
                        val token = user.token
                        nearbyViewModel.findPlaces(lat,lon,token)

                        nearbyViewModel.listPlace.observe(requireActivity()){setPlaceData(it)}
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}