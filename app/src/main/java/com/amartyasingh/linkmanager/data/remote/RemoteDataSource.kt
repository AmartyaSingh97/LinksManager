package com.amartyasingh.linkmanager.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getLinks() = apiService.getData("dashboardNew")
}