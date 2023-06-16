package com.capstoneproject.mytravel.ui.nearby

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.capstoneproject.mytravel.adapter.Nearby
import com.capstoneproject.mytravel.adapter.ReviewUser
import com.capstoneproject.mytravel.adapter.ReviewsAdapter
import com.capstoneproject.mytravel.databinding.ActivityDetailNearbyBinding
import com.capstoneproject.mytravel.model.UserPreference
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
class DetailNearbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNearbyBinding
    private lateinit var detailNearbyViewModel: DetailNearbyViewModel
    private lateinit var adapter: ReviewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNearbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        starRatingSetup()

        val data = intent.getParcelableExtra<Nearby>("DATA")
        val id = data?.id!!.toInt()
        val photo = data.photo
        val photoUrl = "https://storage.googleapis.com/mytravel_bucket/places/$photo"
        val formatRating = String.format("%.1f", data.rating)
        val formatDistance = String.format("%.1f", data.distance)
        val distance = " $formatDistance km"
        val htm = data.price
        Glide.with(this)
            .load(photoUrl)
            .into(binding.imgDetail)

        binding.tvDetailTitle.text = data.name
        binding.tvDetailAddress.text = data.city
        binding.tvDistance.text = distance
        binding.tvHtm.text = " $htm"
        binding.tvDescription.text = data.desc

        detailNearbyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailNearbyViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvReviews.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReviews.addItemDecoration(itemDecoration)


        detailNearbyViewModel.listReviews.observe(this){
            setReviewData(it)
        }

        detailNearbyViewModel.isLoading.observe(this){showLoading(it)}

        detailNearbyViewModel.getUser().observe(this){
            val token = it.token
            val userId = it.userId
            detailNearbyViewModel.getReviews(token, id, userId)
        }

        binding.btnSubmitReview.setOnClickListener{
            detailNearbyViewModel.getUser().observe(this){ user ->
                val idUser = user.userId
                val review = binding.reviewEditText.text.toString()
                val rating = EXTRA_RATING
                val token = user.token
                detailNearbyViewModel.postReview(token, id, idUser, rating, review)
                detailNearbyViewModel.isPostSuccess.observe(this){
                    if(it){
                        Toast.makeText(
                            this,
                            getString(R.string.review_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.reviewEditText.clearFocus()
                        recreate()
                    }
                }
            }
            binding.topProgressBar.visibility = View.VISIBLE
        }

        detailNearbyViewModel.review.observe(this) {review ->
            if(review != null){
                val reviewId = review.id
                val tourismId = review.tourismId
                binding.tvReviewTitle.text = getString(R.string.your_review)
                binding.reviewEditText.setText(review.review.toString())
                binding.reviewEditText.isEnabled = false
                binding.btnSubmitReview.visibility = View.GONE
                binding.btnUpdateReview.visibility = View.VISIBLE
                binding.btnUpdateReview.setOnClickListener{
                    binding.reviewEditText.isEnabled = true
                    binding.btnUpdateReview.visibility = View.GONE
                    binding.btnPostUpdateReview.visibility = View.VISIBLE
                }
                detailNearbyViewModel.getUser().observe(this){
                    val token = it.token
                    val userId = it.userId
                    binding.btnPostUpdateReview.setOnClickListener{
                        val updateReview = binding.reviewEditText.text.toString()
                        detailNearbyViewModel.postUpdateReview(token, tourismId, reviewId, EXTRA_RATING, updateReview, userId)
                        recreate()
                    }
                }
                setStar(review.rating)
            }
        }

        val lat = data.lat!!.toDouble()
        val lon = data.lon!!.toDouble()

        getTemperature(lat,lon)

    }

    private fun setStar(rating: Int){
        when (rating) {
            1 -> {
                buttonEnableOneStar()
                buttonDisableTwoStar()
                buttonDisableThreeStar()
                buttonDisableFourStar()
                buttonDisableFiveStar()
                EXTRA_RATING = 1
            }
            2 -> {
                buttonEnableOneStar()
                buttonEnableTwoStar()
                buttonDisableThreeStar()
                buttonDisableFourStar()
                buttonDisableFiveStar()
                EXTRA_RATING = 2
            }
            3 -> {
                buttonEnableOneStar()
                buttonEnableTwoStar()
                buttonEnableThreeStar()
                buttonDisableFourStar()
                buttonDisableFiveStar()
                EXTRA_RATING = 3
            }
            4 -> {
                buttonEnableOneStar()
                buttonEnableTwoStar()
                buttonEnableThreeStar()
                buttonEnableFourStar()
                buttonDisableFiveStar()
                EXTRA_RATING = 4
            }
            5 -> {
                buttonEnableOneStar()
                buttonEnableTwoStar()
                buttonEnableThreeStar()
                buttonEnableFourStar()
                buttonEnableFiveStar()
                EXTRA_RATING = 5
            }
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
                        val temperature = DecimalFormat("##.#").format(temperatureCelsius)
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
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnOneStar)
    }
    private fun buttonDisableOneStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnOneStar)
    }

    private fun buttonEnableTwoStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnTwoStar)
    }
    private fun buttonDisableTwoStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnTwoStar)
    }

    private fun buttonEnableThreeStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnThreeStar)
    }
    private fun buttonDisableThreeStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnThreeStar)
    }

    private fun buttonEnableFourStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnFourStar)
    }
    private fun buttonDisableFourStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnFourStar)
    }

    private fun buttonEnableFiveStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnFiveStar)
    }
    private fun buttonDisableFiveStar(){
        Glide.with(this@DetailNearbyActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnFiveStar)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.topProgressBar.visibility = View.VISIBLE
        } else {
            binding.topProgressBar.visibility = View.GONE
        }
    }

    companion object{
        var EXTRA_RATING = 0
    }
}