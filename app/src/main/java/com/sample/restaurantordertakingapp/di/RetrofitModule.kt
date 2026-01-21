package com.sample.restaurantordertakingapp.di

import android.content.Context
import com.sample.restaurantordertakingapp.data.remote.ApiService
import com.sample.restaurantordertakingapp.network.LocalJsonInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val BASE_URL = "https://api.example.com/"



    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
      //  val logging = HttpLoggingInterceptor()
       // logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            //.addInterceptor(logging)
            .addInterceptor(LocalJsonInterceptor(appContext))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            // addCallAdapterFactory(...) यदि उपयोग करें
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}