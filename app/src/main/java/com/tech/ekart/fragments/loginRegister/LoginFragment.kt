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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tech.ekart.R
import com.tech.ekart.activities.ShoppingActivity
import com.tech.ekart.databinding.FragmentLoginBinding
import com.tech.ekart.dialog.setupBottomSheetDialog
import com.tech.ekart.util.Resource
import com.tech.ekart.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
            tvDontHaveAccount.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            tvForgotPasswordLogin.setOnClickListener {
                setupBottomSheetDialog {email->
                    viewModel.resetPassword(email)
                }
            }
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.resetPassword.collect{
                        when(it) {
                            is Resource.Loading -> {
                            }

                            is Resource.Success -> {
                                Snackbar.make(requireView(),"Reset link was sent to your email",Snackbar.LENGTH_LONG).show()
                            }

                            is Resource.Error -> {
                                Snackbar.make(requireView(),"Error: ${it.message}",Snackbar.LENGTH_LONG).show()
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.login.collect{
                    when(it){
                        is Resource.Loading->{
                            binding.buttonLoginLogin.startAnimation()
                        }
                        is Resource.Success->{
                            binding.buttonLoginLogin.revertAnimation()
                            Intent(requireActivity(),ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        is Resource.Error->{
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            binding.buttonLoginLogin.revertAnimation()
                        }
                        else -> Unit
                    }
                }
            }
        }

    }
}