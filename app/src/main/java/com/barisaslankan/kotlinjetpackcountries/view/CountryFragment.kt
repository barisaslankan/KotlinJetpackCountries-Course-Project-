package com.barisaslankan.kotlinjetpackcountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.barisaslankan.kotlinjetpackcountries.R
import com.barisaslankan.kotlinjetpackcountries.databinding.FragmentCountryBinding
import util.downloadFromUrl
import util.placeHolderProgressBar
import viewmodel.CountryViewModel


class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : CountryViewModel
    private var countryUUID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            countryUUID = CountryFragmentArgs.fromBundle(it).countryUUID;
        }

        viewModel = ViewModelProvider(this)[CountryViewModel::class.java]
        viewModel.getDataFromRoom(countryUUID)

        observeLiveData()

    }

    fun observeLiveData(){

        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->

            country?.let {
                binding.fragmentCountryCountryNameText.text = country.countryName
                binding.fragmentCountryCountryCapitalText.text = country.countryCapital
                binding.fragmentCountryCountryRegionText.text = country.countryRegion
                binding.fragmentCountryCountryCurrencyText.text = country.countryCurrency
                binding.fragmentCountryCountryLanguageText.text = country.countryLanguage
                context?.let {
                    binding.fragmentCountryCountryFlagImage.downloadFromUrl(
                        country.countryFlagUrl,
                        placeHolderProgressBar(it)
                    )
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}