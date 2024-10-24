package com.example.marsphotos.network

import android.util.Log
import com.example.marsphotos.model.PicsumPhoto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://picsum.photos/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Retrofit service object for creating Picsum API calls.
 */
interface PicsumApiService {
    @GET("v2/list")
    suspend fun getPicsumPhotos(): List<PicsumPhoto>

    @GET("id/{id}/info")
    suspend fun getPicsumPhoto(@Path("id") id: String): PicsumPhoto

    @GET("{width}/{height}")
    suspend fun getBlurredImage(
        @Path("width") width: Int = 200,
        @Path("height") height: Int = 300,
        @Query("blur") blur: String = ""
    ): Response<ResponseBody>

    @GET("{width}/{height}")
    suspend fun getGrayscaleImage(
        @Path("width") width: Int = 200,
        @Path("height") height: Int = 300,
        @Query("grayscale") grayscale: String = ""
    ): Response<ResponseBody>
}

/**
 * Public API object for accessing the lazy-initialized Retrofit service.
 */
object PicsumApi {
    val retrofitService: PicsumApiService by lazy {
        retrofit.create(PicsumApiService::class.java)
    }
}