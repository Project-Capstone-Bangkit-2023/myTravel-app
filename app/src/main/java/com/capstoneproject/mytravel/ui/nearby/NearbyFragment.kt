package com.capstoneproject.mytravel.ui.nearby

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.mytravel.adapter.Nearby
import com.capstoneproject.mytravel.adapter.NearbyAdapter
import com.capstoneproject.mytravel.databinding.FragmentNearbyBinding
import com.capstoneproject.mytravel.retrofit.ItemsItem

class NearbyFragment : Fragment() {

    private var _binding: FragmentNearbyBinding? = null
    private val binding get() = _binding!!
    private lateinit var nearbyViewModel: NearbyViewModel
    private lateinit var adapter: NearbyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNearbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvStory.layoutManager = layoutManager

        nearbyViewModel = ViewModelProvider(this).get(NearbyViewModel::class.java)
        nearbyViewModel.listUser.observe(requireActivity()) { Itemsitem -> setUserData(Itemsitem) }
        nearbyViewModel.isLoading.observe(requireActivity()) { showLoading(it) }

    }

    private fun setUserData(userData: List<ItemsItem>){
        val listNearby = ArrayList<Nearby>()
        for (i in userData) {
            val username = i.login
            val photo = i.avatarUrl
            val url = i.htmlUrl
            val nearby = Nearby(username , photo, url)
            listNearby.add(nearby)
        }
        adapter = NearbyAdapter(listNearby)
        binding.rvStory.adapter = adapter
        adapter.setOnItemClickCallback(object : NearbyAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Nearby) {
                val intentToDetail = Intent(requireContext(), DetailNearbyActivity::class.java)
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