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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}