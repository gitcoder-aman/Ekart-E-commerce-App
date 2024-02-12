package com.tech.ekart.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tech.ekart.R
import com.tech.ekart.adapters.BestProductAdapter
import com.tech.ekart.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    private lateinit var binding : FragmentBaseCategoryBinding
    protected  val offerAdapter : BestProductAdapter by lazy {
        BestProductAdapter()
    }
    protected  val bestProductAdapter : BestProductAdapter by lazy {
        BestProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductsRv()
        binding.rvOfferProduct.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })
        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _->
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                onBestProductsPagingRequest()
            }
        })
    }

    fun showOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }
    fun hideOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.GONE
    }
    fun showBestProductsLoading(){
        binding.bestProductProgressBar.visibility = View.VISIBLE
    }
    fun hideBestProductLoading(){
        binding.bestProductProgressBar.visibility = View.GONE
    }
    open fun onOfferPagingRequest(){

    }
    open fun onBestProductsPagingRequest(){

    }
    private fun setupBestProductsRv() {
        binding.rvBestProduct.apply {
            layoutManager =
                GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = bestProductAdapter
        }
    }

    private fun setupOfferRv() {
        binding.rvOfferProduct.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }
}