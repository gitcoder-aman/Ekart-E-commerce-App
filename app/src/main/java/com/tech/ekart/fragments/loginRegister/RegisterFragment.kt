package com.tech.ekart.fragments.loginRegister

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tech.ekart.R
import com.tech.ekart.data.User
import com.tech.ekart.databinding.FragmentRegisterBinding
import com.tech.ekart.util.RegisterValidation
import com.tech.ekart.util.Resource
import com.tech.ekart.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user  = User(
                    edFirstNameRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )
                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user,password)
            }
            tvOrLogin.setOnClickListener{
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }

        }
//        lifecycleScope.launchWhenStarted {
//            viewModel.register.collect{
//                when(it){
//                    is Resource.Loading->{
//                        binding.buttonRegisterRegister.startAnimation()
//                    }
//                    is Resource.Success->{
//                        Toast.makeText(requireContext(),"Register Successful ${it.message.toString()}", Toast.LENGTH_SHORT).show()
//                    }
//                    is Resource.Error->{
//                        Toast.makeText(requireContext(),"Something went wrong ${it.message.toString()}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.register.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.buttonRegisterRegister.startAnimation()
                        }
                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Register Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.buttonRegisterRegister.revertAnimation()
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "${resource.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.buttonRegisterRegister.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.validation.collect{validation->
                    if(validation.email is RegisterValidation.Failed){
                        withContext(Dispatchers.Main){
                            binding.edEmailRegister.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }
                    if(validation.password is RegisterValidation.Failed){
                        withContext(Dispatchers.Main){
                            binding.edPasswordRegister.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }
                }
            }
        }
    }
}