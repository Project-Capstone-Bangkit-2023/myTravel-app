package com.capstoneproject.mytravel.ui.recommend

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.adapter.Place
import com.capstoneproject.mytravel.adapter.PlaceAdapter
import com.capstoneproject.mytravel.databinding.FragmentRecommendBinding
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.RecommendItem
import com.capstoneproject.mytravel.ui.home.DetailSearchActivity
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.ArrayList


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class RecommendFragment : Fragment() {

    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendViewModel: RecommendViewModel
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecommend.layoutManager = layoutManager

        setupViewModel()

        recommendViewModel.getUser().observe(requireActivity()){user ->
            val token = user.token
            recommendViewModel.findPlaces(token)
        }
        recommendViewModel.listPlace.observe(requireActivity()){setPlaceData(it)}
    }

    private fun setPlaceData(placeData: List<RecommendItem>){
        val listPlace = ArrayList<Place>()
        for (i in placeData) {
            val id = i.id
            val name = i.name
            val category = i.category
            val photo = i.picture
            val desc = i.description
            val city = i.city
            val strRating = i.rating.toString()
            val rating = strRating.toDouble()
            val lat = i.latitude.toDouble()
            val lon = i.longitude.toDouble()
            val price = i.price
            val place = Place(id,name, category, photo, city, rating, price, desc, lat, lon, 0.0)
            listPlace.add(place)
            println(listPlace)
        }
        adapter = PlaceAdapter(requireContext(), listPlace.sortedByDescending { it.rating })
        binding.rvRecommend.adapter = adapter
        adapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                val intentToDetail = Intent(requireActivity(), DetailSearchActivity::class.java)
                intentToDetail.putExtra("DATA", data)
                startActivity(intentToDetail)
            }
        })
    }

    private fun setupViewModel() {
        recommendViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[RecommendViewModel::class.java]

        recommendViewModel.isLoading.observe(requireActivity()){ showLoading(it)}
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}