package com.amartyasingh.linkmanager.data.repository

import com.amartyasingh.linkmanager.data.models.ResponseModel
import com.amartyasingh.linkmanager.data.remote.BaseDataSource
import com.amartyasingh.linkmanager.data.remote.RemoteDataSource
import com.amartyasingh.linkmanager.utils.Resource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class LinksRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): BaseDataSource() {
    suspend fun getLinks() : Flow<Resource<ResponseModel>> {
        return flow {
            emit(getResult { remoteDataSource.getLinks() })
        }.flowOn(Dispatchers.IO)
    }
}