package com.dicoding.com.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.com.storyapp.data.remote.repository.StoryRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: StoryRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getUser() = repository.getUser().asLiveData()
}