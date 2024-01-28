package com.tech.ekart.fragments.loginRegister

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tech.ekart.R
import com.tech.ekart.activities.ShoppingActivity
import com.tech.ekart.databinding.FragmentIntroductionBinding
import com.tech.ekart.util.Resource
import com.tech.ekart.viewmodel.IntroductionViewModel
import com.tech.ekart.viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    private lateinit var binding : FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       lifecycleScope.launch {
           lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
               viewModel.navigate.collectLatest{
                   when (it) {
                       SHOPPING_ACTIVITY -> {
                           Intent(requireContext(),ShoppingActivity::class.java).also {intent->
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                               startActivity(intent)
                           }
                       }
//                       ACCOUNT_OPTIONS_FRAGMENT->{
////                           findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
//                       }
                       else->Unit
                   }
               }
           }
       }
        binding.apply {
            buttonStart.setOnClickListener {
//                viewModel.startButtonClicked()
                findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
            }
        }
    }

}