package com.scribblex.rssi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.scribblex.rssi.data.entities.Rssi
import com.scribblex.rssi.data.repository.RssiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: RssiRepository
) :
    ViewModel() {

    private val viewState: MutableLiveData<List<Rssi>> = MutableLiveData()

    private val observer = Observer<List<Rssi>> {
        setViewState(it)
    }

    override fun onCleared() {
        super.onCleared()
        repository.wirelessScanResults.removeObserver(observer)
    }

    fun observeRssiRepositoryChanges() {
        repository.wirelessScanResults.observeForever(observer)
    }

    fun getViewState(): LiveData<List<Rssi>> {
        return viewState
    }

    fun setViewState(wifiScanResults: List<Rssi>) {
        viewState.value = wifiScanResults
    }

}