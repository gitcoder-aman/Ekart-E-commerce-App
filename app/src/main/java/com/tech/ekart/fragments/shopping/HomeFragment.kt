package com.tech.ekart.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tech.ekart.R
import com.tech.ekart.adapters.HomeViewpagerAdapter
import com.tech.ekart.databinding.FragmentHomeBinding
import com.tech.ekart.fragments.categories.AccessoryFragment
import com.tech.ekart.fragments.categories.ChairFragment
import com.tech.ekart.fragments.categories.CupboardFragment
import com.tech.ekart.fragments.categories.FurnitureFragment
import com.tech.ekart.fragments.categories.MainCategoryFragment
import com.tech.ekart.fragments.categories.TableFragment

class HomeFragment : Fragment(R.layout.fragment_home){
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )
        binding.viewPagerHome.isUserInputEnabled = false // swiping off
        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter

        TabLayoutMediator(binding.tabLayout,binding.viewPagerHome){tab,position->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }
        }.attach()
    }
}