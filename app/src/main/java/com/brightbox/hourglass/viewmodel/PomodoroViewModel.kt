package com.brightbox.hourglass.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PomodoroViewModel(

) : ViewModel() {

    private val _sessionTime = MutableStateFlow("")
    val sessionTime = _sessionTime.asStateFlow()
    private val _breakTime = MutableStateFlow("")
    val breakTime = _breakTime.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val _progressIndicator = MutableStateFlow(0.000001f)
    val progressIndicator = _progressIndicator.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning = _isTimerRunning.asStateFlow()

    private val _isSessionTimeRunning = MutableStateFlow(true)
    val isSessionTimeRunning = _isSessionTimeRunning.asStateFlow()

    private val _isBreakTimeRunning = MutableStateFlow(false)
    val isBreakTimeRunning = _isBreakTimeRunning.asStateFlow()

    var job: Job? = null

    fun startTimer() {
        if (_sessionTime.value.isNotEmpty() && _breakTime.value.isNotEmpty() && !isTimerRunning.value) {
            _isTimerRunning.value = true

            val timeFactor = if (_isSessionTimeRunning.value) {
                sessionTime.value.toFloat()
            } else {
                breakTime.value.toFloat()
            }

//            job?.cancel()

            job = CoroutineScope(Dispatchers.Unconfined).launch {
                Log.d("PomodoroViewModel", "Timer started")
                while (progressIndicator.value < 1) {
                    _progressIndicator.value += 1 / (timeFactor * 6000)
                    _elapsedTime.value += 10
                    delay(10)
                }
                Log.d("PomodoroViewModel", "Timer finished")
                stopTimer()
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        job?.cancel()
    }

    fun stopTimer() {
        Log.d("PomodoroViewModel", "Timer stopped called")
        job?.cancel()
        _progressIndicator.value = 0.000001f
        _elapsedTime.value = 0L
        _isTimerRunning.value = false

        if (_isSessionTimeRunning.value) {
            _isSessionTimeRunning.value = false
            _isBreakTimeRunning.value = true
            Log.d("PomodoroViewModel", "Was in session time. break time = ${_isBreakTimeRunning.value}")
            startTimer()
        } else if (_isBreakTimeRunning.value) {
            job?.cancel()
            _isBreakTimeRunning.value = false
            _isSessionTimeRunning.value = true
        }
    }

    fun updateSessionTime(time: String) {
        _sessionTime.value = time
    }

    fun updateBreakTime(time: String) {
        _breakTime.value = time
    }

}