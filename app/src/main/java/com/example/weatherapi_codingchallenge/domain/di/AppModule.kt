package com.example.weatherapi_codingchallenge.domain.di

import com.example.weatherapi_codingchallenge.data.remote.ApiDetails
import com.example.weatherapi_codingchallenge.data.remote.ApiRequest
import com.example.weatherapi_codingchallenge.data.repository.RepoImpl
import com.example.weatherapi_codingchallenge.data.repository.Repository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideOkHttp(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun provideRetrofit(
        provideOkHttpClient: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiDetails.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(provideOkHttpClient)
            .build()
    }

    @Provides
    fun provideApi(
        retrofit: Retrofit
    ): ApiRequest{
        return retrofit.create(ApiRequest::class.java)
    }

    @Provides
    fun provideRepository(
        apiRequest: ApiRequest
    ): Repository {
        return RepoImpl(apiRequest)
    }
}