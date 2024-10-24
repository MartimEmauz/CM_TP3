package com.example.marsphotos.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.PicsumPhoto
import com.example.marsphotos.network.PicsumApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface PicsumUiState {
    data class Success(val message: String, val randomPhoto: PicsumPhoto) : PicsumUiState
    object Loading : PicsumUiState
    object Error : PicsumUiState
}

class PicsumViewModel : ViewModel() {
    var picsumUiState: PicsumUiState by mutableStateOf(PicsumUiState.Loading)
        private set

    init {
        getPicsumPhotos()
    }

    fun getPicsumPhotos() {
        viewModelScope.launch {
            picsumUiState = PicsumUiState.Loading
            try {
                Log.d("PicsumViewModel", "Fetching Picsum photos...")
                val photos = PicsumApi.retrofitService.getPicsumPhotos()
                val randomPhoto = photos.random()
                picsumUiState = PicsumUiState.Success("Success: ${photos.size} Picsum photos retrieved", randomPhoto)
                Log.d("PicsumViewModel", "Picsum photos retrieved successfully.")
            } catch (e: IOException) {
                Log.e("PicsumViewModel", "IOException while fetching Picsum photos: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            } catch (e: HttpException) {
                Log.e("PicsumViewModel", "HttpException while fetching Picsum photos: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            }
        }
    }

    fun applyBlur(photo: PicsumPhoto) {
        viewModelScope.launch {
            picsumUiState = PicsumUiState.Loading
            try {
                Log.d("PicsumViewModel", "Requesting blurred image for photo ID: ${photo.id}, URL: ${photo.download_url}")
                val response = PicsumApi.retrofitService.getBlurredImage(photo.width, photo.height, "true")
                if (response.isSuccessful) {
                    val blurredImageUrl = response.raw().request.url.toString()
                    Log.d("PicsumViewModel", "Blurred image response successful. URL: $blurredImageUrl")
                    picsumUiState = PicsumUiState.Success("Blurred image retrieved", photo.copy(download_url = blurredImageUrl))
                } else {
                    Log.e("PicsumViewModel", "Blurred image request failed. Error: ${response.errorBody()?.string()}")
                    picsumUiState = PicsumUiState.Error
                }
            } catch (e: IOException) {
                Log.e("PicsumViewModel", "IOException while applying blur: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            } catch (e: HttpException) {
                Log.e("PicsumViewModel", "HttpException while applying blur: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            } catch (e: Exception) {
                Log.e("PicsumViewModel", "Unknown error while applying blur: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            }
        }
    }

    fun applyGrayscale(photo: PicsumPhoto) {
        viewModelScope.launch {
            picsumUiState = PicsumUiState.Loading
            try {
                Log.d("PicsumViewModel", "Requesting grayscale image for photo ID: ${photo.id}, URL: ${photo.download_url}")
                val response = PicsumApi.retrofitService.getGrayscaleImage(photo.width, photo.height, "true")
                if (response.isSuccessful) {
                    val grayscaleImageUrl = response.raw().request.url.toString()
                    Log.d("PicsumViewModel", "Grayscale image response successful. URL: $grayscaleImageUrl")
                    picsumUiState = PicsumUiState.Success("Grayscale image retrieved", photo.copy(download_url = grayscaleImageUrl))
                } else {
                    Log.e("PicsumViewModel", "Grayscale image request failed. Error: ${response.errorBody()?.string()}")
                    picsumUiState = PicsumUiState.Error
                }
            } catch (e: IOException) {
                Log.e("PicsumViewModel", "IOException while applying grayscale: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            } catch (e: HttpException) {
                Log.e("PicsumViewModel", "HttpException while applying grayscale: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            } catch (e: Exception) {
                Log.e("PicsumViewModel", "Unknown error while applying grayscale: ${e.message}", e)
                picsumUiState = PicsumUiState.Error
            }
        }
    }
}
