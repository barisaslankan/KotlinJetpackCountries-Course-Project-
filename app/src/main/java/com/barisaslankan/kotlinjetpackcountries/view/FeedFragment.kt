package com.barisaslankan.kotlinjetpackcountries.view

import adapter.CountryAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.barisaslankan.kotlinjetpackcountries.R
import com.barisaslankan.kotlinjetpackcountries.databinding.FragmentCountryBinding
import com.barisaslankan.kotlinjetpackcountries.databinding.FragmentFeedBinding
import viewmodel.FeedViewModel

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        viewModel.refreshData()

        binding.fragmentFeedRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.fragmentFeedRecyclerView.adapter = countryAdapter

        observeLiveData()

        binding.feedFragmentSwipeRefreshLayout.setOnRefreshListener {
            binding.fragmentFeedProgressBar.visibility = View.VISIBLE
            binding.fragmentFeedRecyclerView.visibility = View.GONE
            binding.fragmentFeedErrorText.visibility = View.GONE
            viewModel.refreshFromAPI()
        }

    }

    fun observeLiveData(){

        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->

            countries?.let {
                binding.fragmentFeedRecyclerView.visibility = View.VISIBLE
                countryAdapter.updateCountryList(it)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->

            error?.let {
                if (it){
                    binding.fragmentFeedErrorText.visibility = View.VISIBLE
                    binding.fragmentFeedRecyclerView.visibility = View.GONE
                    binding.fragmentFeedProgressBar.visibility = View.GONE
                }else{
                    binding.fragmentFeedErrorText.visibility = View.GONE
                    binding.fragmentFeedRecyclerView.visibility = View.VISIBLE
                    binding.fragmentFeedErrorText.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { isLoading ->

            isLoading?.let {
                if (it){
                    binding.fragmentFeedProgressBar.visibility = View.VISIBLE
                    binding.fragmentFeedRecyclerView.visibility = View.GONE
                    binding.fragmentFeedErrorText.visibility = View.GONE
                }else{
                    binding.fragmentFeedProgressBar.visibility = View.GONE
                    binding.fragmentFeedRecyclerView.visibility = View.VISIBLE
                    binding.fragmentFeedErrorText.visibility = View.GONE
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}