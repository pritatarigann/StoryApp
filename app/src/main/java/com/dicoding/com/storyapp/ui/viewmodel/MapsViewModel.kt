package com.dicoding.com.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.com.storyapp.data.ResultState
import com.dicoding.com.storyapp.data.model.StoryModel
import com.dicoding.com.storyapp.data.remote.repository.StoryRepository


class MapsViewModel(repository: StoryRepository) : ViewModel() {
    val listStoryWithLocation: LiveData<ResultState<List<StoryModel>>> = repository.getStoriesWithLocation()
}