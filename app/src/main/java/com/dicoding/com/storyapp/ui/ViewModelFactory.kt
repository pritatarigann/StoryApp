package com.dicoding.com.storyapp.ui

import com.dicoding.com.storyapp.data.remote.repository.StoryRepository
import com.dicoding.com.storyapp.di.Injection
import com.dicoding.com.storyapp.ui.viewmodel.AddStoryViewModel
import com.dicoding.com.storyapp.ui.viewmodel.HomeViewModel
import com.dicoding.com.storyapp.ui.viewmodel.LoginViewModel
import com.dicoding.com.storyapp.ui.viewmodel.MapsViewModel
import com.dicoding.com.storyapp.ui.viewmodel.ProfileViewModel
import com.dicoding.com.storyapp.ui.viewmodel.RegisterViewModel
import com.dicoding.com.storyapp.ui.viewmodel.SplashScreenViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelFactory(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> {
                SplashScreenViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}