package com.capstoneproject.mytravel.ui.gem

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.mytravel.HomeActivity
import com.capstoneproject.mytravel.adapter.Gem
import com.capstoneproject.mytravel.adapter.GemAdapter
import com.capstoneproject.mytravel.databinding.FragmentGemBinding
import com.capstoneproject.mytravel.retrofit.ItemsItem

class GemFragment : Fragment() {
    
    private var _binding: FragmentGemBinding? = null
    private val binding get() = _binding!!
    private lateinit var gemViewModel: GemViewModel
    private lateinit var adapter: GemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvStory.layoutManager = layoutManager

        gemViewModel = ViewModelProvider(this).get(GemViewModel::class.java)
        gemViewModel.listUser.observe(requireActivity()) { Itemsitem -> setUserData(Itemsitem) }
        gemViewModel.isLoading.observe(requireActivity()) { showLoading(it) }

    }

    private fun setUserData(userData: List<ItemsItem>){
        val listGem = ArrayList<Gem>()
        for (i in userData) {
            val username = i.login
            val photo = i.avatarUrl
            val url = i.htmlUrl
            val gem = Gem(username , photo, url)
            listGem.add(gem)
        }
        adapter = GemAdapter(listGem)
        binding.rvStory.adapter = adapter
        adapter.setOnItemClickCallback(object : GemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Gem) {
                val intentToDetail = Intent(requireContext(), DetailGemActivity::class.java)
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