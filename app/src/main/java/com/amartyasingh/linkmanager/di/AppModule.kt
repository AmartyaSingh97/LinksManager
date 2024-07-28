package com.amartyasingh.linkmanager.di

import com.amartyasingh.linkmanager.data.remote.ApiService
import com.amartyasingh.linkmanager.data.remote.Interceptor
import com.amartyasingh.linkmanager.data.remote.RemoteDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.inopenapp.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor()
    }



    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)

    @Provides
    fun provideApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource) =
        com.amartyasingh.linkmanager.data.repository.LinksRepository(remoteDataSource)
}