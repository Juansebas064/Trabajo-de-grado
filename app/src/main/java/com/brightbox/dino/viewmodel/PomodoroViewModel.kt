package com.brightbox.dino.viewmodel

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
    private val _numberOfSessions = MutableStateFlow("")
    val numberOfSessions = _numberOfSessions.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()
    private val _elapsedNumberOfSessions = MutableStateFlow(0)
    val elapsedNumberOfSessions = _elapsedNumberOfSessions.asStateFlow()

    private val _progressIndicator = MutableStateFlow(0.000001f)
    val progressIndicator = _progressIndicator.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning = _isTimerRunning.asStateFlow()

    private val _isSessionTimeRunning = MutableStateFlow(false)
    val isSessionTimeRunning = _isSessionTimeRunning.asStateFlow()

    private val _isBreakTimeRunning = MutableStateFlow(false)
    val isBreakTimeRunning = _isBreakTimeRunning.asStateFlow()

    var job: Job? = null

    fun startTimer() {
        if (_sessionTime.value.isNotEmpty()
            && _breakTime.value.isNotEmpty()
            && _numberOfSessions.value.isNotEmpty()
            && !isTimerRunning.value
        ) {
            if (_elapsedNumberOfSessions.value == 0) {
                _isSessionTimeRunning.value = true
                _elapsedNumberOfSessions.value = 1
            }

            _isTimerRunning.value = true

            val timeFactor = if (_isSessionTimeRunning.value) {
                sessionTime.value.toFloat()
            } else {
                breakTime.value.toFloat()
            }

            job = CoroutineScope(Dispatchers.Unconfined).launch {
                while (progressIndicator.value < 1) {
                    _progressIndicator.value += 1 / (timeFactor * 6000)
                    _elapsedTime.value += 10
                    delay(10)
                }
                stopTimer()
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        job?.cancel()
    }

    fun stopTimer() {
        job?.cancel()
        _progressIndicator.value = 0.000001f
        _elapsedTime.value = 0L
        _isTimerRunning.value = false

        if (_isSessionTimeRunning.value) {
            _isSessionTimeRunning.value = false
            if (_elapsedNumberOfSessions.value < numberOfSessions.value.toInt()) {
                _isBreakTimeRunning.value = true
                startTimer()
            } else {
                _elapsedNumberOfSessions.value = 0
            }
        } else if (_isBreakTimeRunning.value) {
            _isBreakTimeRunning.value = false
            _isSessionTimeRunning.value = true
            if (_elapsedNumberOfSessions.value < numberOfSessions.value.toInt()) {
                _elapsedNumberOfSessions.value += 1
                startTimer()
            } else {
                _elapsedNumberOfSessions.value = 0
            }
        }
    }

    fun cancelTimer() {
        job?.cancel()
        _progressIndicator.value = 0.000001f
        _elapsedTime.value = 0L
        _isTimerRunning.value = false
        _isSessionTimeRunning.value = false
        _isBreakTimeRunning.value = false
        _elapsedNumberOfSessions.value = 0
    }

    fun updateSessionTime(time: String) {
        _sessionTime.value = time
    }

    fun updateBreakTime(time: String) {
        _breakTime.value = time
    }

    fun updateNumberOfSessions(numberOfSessions: String) {
        _numberOfSessions.value = numberOfSessions
    }

}