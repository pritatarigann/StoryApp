package com.dicoding.com.storyapp.ui.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.com.storyapp.databinding.ActivitySplashScreenBinding
import com.dicoding.com.storyapp.ui.ViewModelFactory
import com.dicoding.com.storyapp.ui.viewmodel.SplashScreenViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val viewModel by viewModels<SplashScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(3000)

                viewModel.getUser().observe(this@SplashScreenActivity) { user ->
                    if (!user.isLogin) {
                        startActivity(
                            Intent(
                                this@SplashScreenActivity,
                                WelcomeActivity::class.java
                            )
                        )
                    } else {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    }
                    finish()
                }
            }
    }
}