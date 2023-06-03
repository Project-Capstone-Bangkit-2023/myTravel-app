package com.capstoneproject.mytravel.ui.recommend

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.mytravel.adapter.Recommend
import com.capstoneproject.mytravel.adapter.RecommendAdapter
import com.capstoneproject.mytravel.databinding.FragmentRecommendBinding
import com.capstoneproject.mytravel.retrofit.ItemsItem


class RecommendFragment : Fragment() {

    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendViewModel: RecommendViewModel
    private lateinit var adapter: RecommendAdapter

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
        binding.rvStory.layoutManager = layoutManager

        recommendViewModel = ViewModelProvider(this).get(RecommendViewModel::class.java)
        recommendViewModel.listUser.observe(requireActivity()) { Itemsitem -> setUserData(Itemsitem) }
        recommendViewModel.isLoading.observe(requireActivity()) { showLoading(it) }

    }

    private fun setUserData(userData: List<ItemsItem>){
        val listRecommend = ArrayList<Recommend>()
        for (i in userData) {
            val username = i.login
            val photo = i.avatarUrl
            val url = i.htmlUrl
            val weather = "26C"
            val recommend = Recommend(username , photo, url, weather)
            listRecommend.add(recommend)
        }
        adapter = RecommendAdapter(listRecommend)
        binding.rvStory.adapter = adapter
        adapter.setOnItemClickCallback(object : RecommendAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recommend) {
                val intentToDetail = Intent(requireContext(), DetailRecommendActivity::class.java)
                intentToDetail.putExtra("DATA", data)
                println(data)
                startActivity(intentToDetail)
            }
        })

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