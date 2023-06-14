package com.capstoneproject.mytravel.ui.home


import androidx.appcompat.widget.SearchView
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
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
import androidx.viewpager.widget.ViewPager
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.adapter.Place
import com.capstoneproject.mytravel.adapter.PlaceAdapter
import com.capstoneproject.mytravel.adapter.SlideshowAdapter
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

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }

        setupViewModel()

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.edtSearch

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Search Here..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.edtSearch.clearFocus()
                homeViewModel.getUser().observe(requireActivity()) { user ->
                    val token = user.token
                    homeViewModel.findPlaces(token, query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
        setSlideshow(true)
    }

    override fun onPause(){
        super.onPause()
        setSlideshow(false)
    }
    override fun onResume(){
        super.onResume()
        setSlideshow(true)
    }
    private fun setSlideshow(play: Boolean){
        if(play){
            val images = listOf(R.drawable.bahari, R.drawable.budaya, R.drawable.cagaralam, R.drawable.tempatibadah, R.drawable.tamanhiburan, R.drawable.pusatperbelanjaan)
            val adapter = SlideshowAdapter(requireActivity(), images)
            binding.viewPager.adapter = adapter

            val delay: Long = 3000
            var currentPage = 0
            val handler = Handler()
            val runnable = object : Runnable {
                val NUM_PAGES = images.size

                override fun run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0
                    }
                    getCircle(currentPage)
                    binding.viewPager.setCurrentItem(currentPage++, true)
                    handler.postDelayed(this, delay)
                }
            }

            val onPageChangeListener = object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, delay)
                }

                override fun onPageSelected(position: Int) {
                    currentPage = position
                    getCircle(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    handler.removeCallbacks(runnable)
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        handler.postDelayed(runnable, delay)
                    }
                }
            }
            binding.viewPager.addOnPageChangeListener(onPageChangeListener)
        }
    }

    private fun getCircle(currentPage: Int){
        when (currentPage) {
            0 -> {
                getImg1()
            }
            1 -> {
                getImg2()
            }
            2 -> {
                getImg3()
            }
            3 -> {
                getImg4()
            }
            4 -> {
                getImg5()
            }
            5 -> {
                getImg6()
            }
        }
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
                binding.cvSlideshow.visibility = View.VISIBLE
            }else{
                binding.cvRecommend.visibility = View.GONE
                binding.cvNearby.visibility = View.GONE
                binding.cvGem.visibility = View.GONE
                binding.cvSlideshow.visibility = View.GONE
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

    private fun getImg1(){
        binding.img1.setImageResource(R.drawable.baseline_circle_20)
        binding.img2.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img3.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img4.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img5.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img6.setImageResource(R.drawable.baseline_circle_20_grey)
    }

    private fun getImg2(){
        binding.img1.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img2.setImageResource(R.drawable.baseline_circle_20)
        binding.img3.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img4.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img5.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img6.setImageResource(R.drawable.baseline_circle_20_grey)
    }

    private fun getImg3(){
        binding.img1.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img2.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img3.setImageResource(R.drawable.baseline_circle_20)
        binding.img4.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img5.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img6.setImageResource(R.drawable.baseline_circle_20_grey)
    }
    private fun getImg4(){
        binding.img1.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img2.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img3.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img4.setImageResource(R.drawable.baseline_circle_20)
        binding.img5.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img6.setImageResource(R.drawable.baseline_circle_20_grey)
    }
    private fun getImg5(){
        binding.img1.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img2.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img3.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img4.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img5.setImageResource(R.drawable.baseline_circle_20)
        binding.img6.setImageResource(R.drawable.baseline_circle_20_grey)
    }
    private fun getImg6(){
        binding.img1.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img2.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img3.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img4.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img5.setImageResource(R.drawable.baseline_circle_20_grey)
        binding.img6.setImageResource(R.drawable.baseline_circle_20)
    }

}