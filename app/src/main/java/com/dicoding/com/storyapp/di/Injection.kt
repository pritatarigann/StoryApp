package com.dicoding.com.storyapp.di

import android.content.Context
import com.dicoding.com.storyapp.data.local.room.StoryDatabase
import com.dicoding.com.storyapp.data.pref.UserPreference
import com.dicoding.com.storyapp.data.pref.dataStore
import com.dicoding.com.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.com.storyapp.data.remote.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(database, apiService, pref)
    }
}