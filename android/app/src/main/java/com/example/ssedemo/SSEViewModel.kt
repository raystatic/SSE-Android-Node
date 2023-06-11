package com.example.ssedemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SSEViewModel: ViewModel() {

    private val repository = SSERepository()

    var sseEvents = MutableLiveData<SSEEventData>()
    private set

    fun getSSEEvents() = viewModelScope.launch {
        repository.sseEventsFlow
            .onEach { sseEventData ->
                sseEvents.postValue(sseEventData)
            }
            .catch {
                sseEvents.postValue(SSEEventData(status = STATUS.ERROR))
            }
            .launchIn(viewModelScope)
    }

}