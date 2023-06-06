package com.capstoneproject.mytravel.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.BuildConfig
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.ViewModelFactory
import com.capstoneproject.mytravel.adapter.Place
import com.capstoneproject.mytravel.adapter.PlaceAdapter
import com.capstoneproject.mytravel.adapter.ReviewUser
import com.capstoneproject.mytravel.adapter.ReviewsAdapter
import com.capstoneproject.mytravel.databinding.ActivityDetailSearchBinding
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.DataItem
import com.capstoneproject.mytravel.retrofit.TourismRatingItem
import com.capstoneproject.mytravel.retrofit.WeatherResponse
import com.capstoneproject.mytravel.retrofit.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.util.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DetailSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSearchBinding
    private lateinit var detailSearchViewModel: DetailSearchViewModel
    private lateinit var adapter: ReviewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Place>("DATA")
        val id = data?.id!!.toInt()
        val photo = data.photo
        val photoUrl = "https://storage.googleapis.com/mytravel_bucket/places/$photo"

        Glide.with(this)
            .load(photoUrl)
            .into(binding.imgDetail)

        binding.tvDetailTitle.text = data.name
        binding.tvDetailAddress.text = data.city
        binding.tvRating.text = data.rating.toString()
        binding.tvHtm.text = data.price.toString()
        binding.tvDescription.text = data.desc

        detailSearchViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailSearchViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvReviews.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReviews.addItemDecoration(itemDecoration)

        detailSearchViewModel.listReviews.observe(this){
            setReviewData(it)
        }

        detailSearchViewModel.getUser().observe(this){
            val token = it.token
            detailSearchViewModel.getReviews(token, id)
        }

        val lat = data.lat!!.toDouble()
        val lon = data.lon!!.toDouble()
        
        getTemperature(lat,lon)
        starRatingSetup()

        binding.btnSubmitReview.setOnClickListener{
            println(EXTRA_RATING)
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
    private fun setReviewData(reviewData: List<TourismRatingItem>){
        val listReview = ArrayList<ReviewUser>()
        for (i in reviewData) {
            val id = i.id
            val name = i.user.name
            val rating = i.rating
            var review = ""
            review = if(i.review != null){
                i.review.toString()
            }else{
                ""
            }
            val userId = i.userId
            val reviewUser = ReviewUser(name, review, rating, id,userId)
            listReview.add(reviewUser)
        }
        adapter = ReviewsAdapter(listReview)
        binding.rvReviews.adapter = adapter
        adapter.setOnItemClickCallback()
    }


    private fun starRatingSetup(){
        binding.btnOneStar.setOnClickListener{
            binding.let {
                EXTRA_RATING = if (EXTRA_RATING == 1){
                    buttonDisableOneStar()
                    0
                }else{
                    buttonEnableOneStar()
                    buttonDisableTwoStar()
                    buttonDisableThreeStar()
                    buttonDisableFourStar()
                    buttonDisableFiveStar()
                    1
                }
            }
        }

        binding.btnTwoStar.setOnClickListener{
            binding.let {
                EXTRA_RATING = if (EXTRA_RATING == 2){
                    buttonDisableOneStar()
                    buttonDisableTwoStar()
                    0
                }else{
                    buttonEnableOneStar()
                    buttonEnableTwoStar()
                    buttonDisableThreeStar()
                    buttonDisableFourStar()
                    buttonDisableFiveStar()
                    2
                }
            }
        }

        binding.btnThreeStar.setOnClickListener{
            binding.let {
                EXTRA_RATING = if (EXTRA_RATING == 3){
                    buttonDisableOneStar()
                    buttonDisableTwoStar()
                    buttonDisableThreeStar()
                    0
                }else{
                    buttonEnableOneStar()
                    buttonEnableTwoStar()
                    buttonEnableThreeStar()
                    buttonDisableFourStar()
                    buttonDisableFiveStar()
                    3
                }
            }
        }

        binding.btnFourStar.setOnClickListener{
            binding.let {
                EXTRA_RATING = if (EXTRA_RATING == 4){
                    buttonDisableOneStar()
                    buttonDisableTwoStar()
                    buttonDisableThreeStar()
                    buttonDisableFourStar()
                    0
                }else{
                    buttonEnableOneStar()
                    buttonEnableTwoStar()
                    buttonEnableThreeStar()
                    buttonEnableFourStar()
                    buttonDisableFiveStar()
                    4
                }
            }
        }

        binding.btnFiveStar.setOnClickListener{
            binding.let {
                EXTRA_RATING = if (EXTRA_RATING == 5){
                    buttonDisableOneStar()
                    buttonDisableTwoStar()
                    buttonDisableThreeStar()
                    buttonDisableFourStar()
                    buttonDisableFiveStar()
                    0
                }else{
                    buttonEnableOneStar()
                    buttonEnableTwoStar()
                    buttonEnableThreeStar()
                    buttonEnableFourStar()
                    buttonEnableFiveStar()
                    5
                }
            }
        }
    }

    private fun buttonEnableOneStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnOneStar)
    }
    private fun buttonDisableOneStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnOneStar)
    }

    private fun buttonEnableTwoStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnTwoStar)
    }
    private fun buttonDisableTwoStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnTwoStar)
    }

    private fun buttonEnableThreeStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnThreeStar)
    }
    private fun buttonDisableThreeStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnThreeStar)
    }

    private fun buttonEnableFourStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnFourStar)
    }
    private fun buttonDisableFourStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnFourStar)
    }

    private fun buttonEnableFiveStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnFiveStar)
    }
    private fun buttonDisableFiveStar(){
        Glide.with(this@DetailSearchActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnFiveStar)
    }

    companion object{
        var EXTRA_RATING = 0
    }
}