package com.dicoding.com.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.com.storyapp.data.remote.repository.StoryRepository

class RegisterViewModel(private val repository: StoryRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = repository.register(name, email, password)
}