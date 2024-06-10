package com.dicoding.com.storyapp.ui.viewmodel

import com.dicoding.com.storyapp.data.model.StoryModel
import com.dicoding.com.storyapp.data.remote.repository.StoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn


class HomeViewModel(repository: StoryRepository) : ViewModel() {
    val listStory:  LiveData<PagingData<StoryModel>> = repository.getStories().cachedIn(viewModelScope)
}