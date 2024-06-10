package com.dicoding.com.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.com.storyapp.data.remote.repository.StoryRepository

class SplashScreenViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getUser() = repository.getUser().asLiveData()
}