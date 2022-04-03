package com.scribblex.rssi.di

import android.content.Context
import com.scribblex.rssi.data.local.RssiDataSource
import com.scribblex.rssi.data.remote.ApiService
import com.scribblex.rssi.data.remote.MockInterceptor
import com.scribblex.rssi.data.remote.RemoteDataSource
import com.scribblex.rssi.data.repository.RssiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = setupRetrofit()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)

    @Singleton
    @Provides
    fun provideRssiDataSource(@ApplicationContext context: Context) = RssiDataSource(context)

    @Singleton
    @Provides
    fun provideRssiRepository(rssiDataSource: RssiDataSource) = RssiRepository(rssiDataSource)

    private fun setupRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(MockInterceptor())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://www.mocky.io")
            .client(client)
            .build()
    }

}