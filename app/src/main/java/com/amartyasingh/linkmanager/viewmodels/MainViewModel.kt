package com.amartyasingh.linkmanager.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amartyasingh.linkmanager.data.models.LinksModel
import com.amartyasingh.linkmanager.data.models.ResponseModel
import com.amartyasingh.linkmanager.data.repository.LinksRepository
import com.amartyasingh.linkmanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val linksRepository: LinksRepository,application: Application):
    AndroidViewModel(application){

     private val _linksData: MutableLiveData<Resource<ResponseModel>> = MutableLiveData()

     private val _topLinks: MutableLiveData<Resource<List<LinksModel>>> = MutableLiveData()

     private val _recentLinks: MutableLiveData<Resource<List<LinksModel>>> = MutableLiveData()

    private val _chartData: MutableLiveData<Resource<Map<String, Long>>> = MutableLiveData()

    fun getTopLinks(): LiveData<Resource<List<LinksModel>>> = _topLinks
    fun getRecentLinks(): LiveData<Resource<List<LinksModel>>> = _recentLinks
    fun getChartData(): LiveData<Resource<Map<String, Long>>> = _chartData
    fun getLinksData(): LiveData<Resource<ResponseModel>> = _linksData





    fun fetchLinks() = viewModelScope.launch {
        linksRepository.getLinks().collectLatest {
            when (it.status) {
                Resource.Status.LOADING  -> {
                    _linksData.postValue(Resource.loading(null))
                    _recentLinks.postValue(Resource.loading(null))
                    _topLinks.postValue(Resource.loading(null))
                    _chartData.postValue(Resource.loading(null))

                }

                Resource.Status.SUCCESS -> {
                    it.data?.let { data ->
                        _linksData.postValue(Resource.success(data))
                        _topLinks.postValue(Resource.success(data.data.top_links))
                        _recentLinks.postValue(Resource.success(data.data.recent_links))
                        _chartData.postValue(Resource.success(data.data.overall_url_chart))
                    }
                }

                Resource.Status.ERROR -> {
                    _linksData.postValue(Resource.error(it.message!!, null))
                    _topLinks.postValue(Resource.error(it.message, null))
                    _recentLinks.postValue(Resource.error(it.message, null))
                    _chartData.postValue(Resource.error(it.message, null))
                }
            }

        }
    }

    fun greetings(): String {
        val date = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        return when (cal.get(Calendar.HOUR_OF_DAY)) {
            in 12..16 -> {
                "Good Afternoon"
            }

            in 17..23 -> {
                "Good Evening"
            }

            else -> {
                "Good Morning"
            }
        }
    }


}