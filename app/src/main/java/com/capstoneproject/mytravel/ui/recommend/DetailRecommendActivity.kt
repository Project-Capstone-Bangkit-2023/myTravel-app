package com.capstoneproject.mytravel.ui.recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.adapter.Recommend
import com.capstoneproject.mytravel.adapter.ReviewsAdapter
import com.capstoneproject.mytravel.databinding.ActivityDetailRecommendBinding
import com.capstoneproject.mytravel.retrofit.FollowerResponseItem


@Suppress("DEPRECATION")
class DetailRecommendActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailRecommendBinding
    private lateinit var recommendReviewsViewModel: RecommendReviewsViewModel
    private lateinit var adapter: ReviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val data = intent.getParcelableExtra<Recommend>("DATA")
        binding.tvDetailRecommend.text = data?.name
        binding.tvDetailAddress.text = data?.url
        binding.let {
            Glide.with(this@DetailRecommendActivity)
                .load(data?.photo)
                .into(it.imgDetailRecommend)
        }

        starRatingSetup()

        recommendReviewsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RecommendReviewsViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvReviews.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReviews.addItemDecoration(itemDecoration)
        data?.name?.let { recommendReviewsViewModel.findUser(it) }

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
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnOneStar)
    }
    private fun buttonDisableOneStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnOneStar)
    }

    private fun buttonEnableTwoStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnTwoStar)
    }
    private fun buttonDisableTwoStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnTwoStar)
    }

    private fun buttonEnableThreeStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnThreeStar)
    }
    private fun buttonDisableThreeStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnThreeStar)
    }

    private fun buttonEnableFourStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnFourStar)
    }
    private fun buttonDisableFourStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnFourStar)
    }

    private fun buttonEnableFiveStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_30)
            .into(binding.btnFiveStar)
    }
    private fun buttonDisableFiveStar(){
        Glide.with(this@DetailRecommendActivity)
            .load(R.drawable.baseline_star_border_30)
            .into(binding.btnFiveStar)
    }


    companion object{
        var EXTRA_RATING = 0
    }

}