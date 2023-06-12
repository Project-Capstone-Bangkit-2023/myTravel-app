package com.capstoneproject.mytravel.ui.home


import androidx.appcompat.widget.SearchView
import android.app.SearchManager
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.adapter.Place
import com.capstoneproject.mytravel.adapter.PlaceAdapter
import com.capstoneproject.mytravel.databinding.FragmentHomeBinding
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.DataItem
import com.capstoneproject.mytravel.ui.setting.SettingActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: PlaceAdapter

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

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvPlace.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvPlace.addItemDecoration(itemDecoration)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireActivity(), Locale.getDefault())

        binding.btnSetting.setOnClickListener{
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }

        setupViewModel()

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.edtSearch

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Search Here..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.edtSearch.clearFocus()
                homeViewModel.getUser().observe(requireActivity()){ user ->
                    val token = user.token
                    homeViewModel.findPlaces(token, query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
    }

    private fun setPlaceData(placeData: List<DataItem>){
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
        adapter = PlaceAdapter(listPlace)
        binding.rvPlace.adapter = adapter
        adapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                val intentToDetail = Intent(requireActivity(), DetailSearchActivity::class.java)
                intentToDetail.putExtra("DATA", data)
                println(data)
                startActivity(intentToDetail)
            }
        })
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[HomeViewModel::class.java]

        homeViewModel.listPlace.observe(requireActivity()) {DataItem ->
            if(DataItem == null){
                binding.cvRecommend.visibility = View.VISIBLE
                binding.cvNearby.visibility = View.VISIBLE
                binding.cvGem.visibility = View.VISIBLE
            }else{
                binding.cvRecommend.visibility = View.GONE
                binding.cvNearby.visibility = View.GONE
                binding.cvGem.visibility = View.GONE
                setPlaceData(DataItem)
            }
        }

        homeViewModel.isLoading.observe(requireActivity()){ showLoading(it)}
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}