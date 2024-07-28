package com.amartyasingh.linkmanager.data.remote

import com.amartyasingh.linkmanager.data.models.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{endPoint}")
    suspend fun getData(@Path("endPoint")endPoint:String): Response<ResponseModel>
}