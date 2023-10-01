package com.sameh.stopwatch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopWatchViewModel : ViewModel() {

    private val _elapsedTimeMillisMutableLiveData = MutableLiveData<Long>()
    val elapsedTimeMillisLiveData: LiveData<Long> = _elapsedTimeMillisMutableLiveData

    private val isTimeRunningMutableLiveData = MutableLiveData(false)
    val isTimeRunningLiveData: LiveData<Boolean> = isTimeRunningMutableLiveData

    private var startTime: Long = 0
    private var isRunning = false

    init {
        _elapsedTimeMillisMutableLiveData.value = 0
    }

    fun start() {
        Log.d(Constants.AppTAG, "start: isRunning: $isRunning")
        if (!isRunning) {
            Log.d(Constants.AppTAG, "start: inside")
            val currentTime = System.currentTimeMillis()
            startTime = currentTime - (_elapsedTimeMillisMutableLiveData.value ?: 0)
            isRunning = true
            isTimeRunningMutableLiveData.postValue(true)

            viewModelScope.launch(Dispatchers.Default) {
                while (isRunning) {
                    val currentTimeInsideLoop = System.currentTimeMillis()
                    _elapsedTimeMillisMutableLiveData.postValue(currentTimeInsideLoop - startTime)
                    delay(500)
                }
            }
        }
    }

    fun stop() {
        isRunning = false
        isTimeRunningMutableLiveData.postValue(false)
    }

    fun reset() {
        isRunning = false
        isTimeRunningMutableLiveData.postValue(false)
        _elapsedTimeMillisMutableLiveData.postValue(0)
    }
}
