package com.dicoding.com.storyapp.data.remote.repository

import com.dicoding.com.storyapp.data.ResultState
import com.dicoding.com.storyapp.data.local.room.StoryDatabase
import com.dicoding.com.storyapp.data.model.StoryModel
import com.dicoding.com.storyapp.data.model.UserModel
import com.dicoding.com.storyapp.data.pref.UserPreference
import com.dicoding.com.storyapp.data.remote.response.BaseResponse
import com.dicoding.com.storyapp.data.remote.response.LoginResponse
import com.dicoding.com.storyapp.data.remote.response.StoryResponse
import com.dicoding.com.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.com.storyapp.data.remote.retrofit.ApiService
import com.dicoding.com.storyapp.util.wrapEspressoIdlingResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private var apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        wrapEspressoIdlingResource {
            try {
                val successResponse = apiService.login(email, password)
                val newApiService = ApiConfig.getApiService(successResponse.loginResult.token)
                apiService = newApiService
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            }
        }
    }

    fun getStories(): LiveData<PagingData<StoryModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }

        ).liveData.map { pagingData ->
            pagingData.map { listStoryItem ->
                StoryModel(
                    listStoryItem.id,
                    listStoryItem.name,
                    listStoryItem.description,
                    listStoryItem.photoUrl
                )
            }
        }
    }

    fun getStoriesWithLocation() = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            val stories = response.listStory
            if (stories.isEmpty()) {
                emit(ResultState.Empty)
            } else {
                val storyList = stories.map { story ->
                    StoryModel(
                        story.id,
                        story.name,
                        story.description,
                        story.photoUrl,
                        story.lat,
                        story.lon
                    )
                }
                emit(ResultState.Success(storyList))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun addNewStory(
        imageFile: File,
        description: String,
        latitude: Double? = null,
        longitude: Double? = null
    ) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val requestLatitude = latitude?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestLongitude = longitude?.toString()?.toRequestBody("text/plain".toMediaType())

        try {
            val successResponse = if (requestLatitude != null && requestLongitude != null) {
                apiService.addStory(multipartBody, requestBody, requestLatitude, requestLongitude)
            } else {
                apiService.addStory(multipartBody, requestBody)
            }
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun saveUser(user: UserModel) {
        userPreference.saveUser(user)
    }

    fun getUser(): Flow<UserModel> {
        return userPreference.getUser()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
            userPreference: UserPreference
        ) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase, apiService, userPreference)
            }.also { instance = it }
    }
}