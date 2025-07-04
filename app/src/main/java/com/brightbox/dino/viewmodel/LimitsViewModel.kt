package com.brightbox.dino.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.dino.events.LimitsEvent
import com.brightbox.dino.model.ApplicationsModel
import com.brightbox.dino.model.LimitsModel
import com.brightbox.dino.model.states.LimitsState
import com.brightbox.dino.model.usecases.ApplicationsUseCase
import com.brightbox.dino.model.usecases.LimitsUseCase
import com.brightbox.dino.utils.formatMillisecondsToSQLiteDate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LimitsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val _limitsUseCase: LimitsUseCase,
    private val _applicationsUseCase: ApplicationsUseCase
) : ViewModel() {

    val resetLimitsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
            viewModelScope.launch {
                _limitsUseCase.resetLimitsAtMidnight(_state.value.limitsList)
            }
        }
    }

    private val _isSystemAlertWindowPermissionGranted = MutableStateFlow(false)
    val isSystemAlertWindowPermissionGranted = _isSystemAlertWindowPermissionGranted.asStateFlow()

    private val _isUsageAccessPermissionGranted = MutableStateFlow(false)
    val isUsageAccessPermissionGranted = _isUsageAccessPermissionGranted.asStateFlow()

    private val _state = MutableStateFlow(LimitsState())

    private val _appsList = MutableStateFlow(listOf<ApplicationsModel>())

    private val _icons = MutableStateFlow(mapOf<String, Drawable>())

    private val _limitsList = _limitsUseCase.getLimits()

    val state =
        combine(_state, _appsList, _icons, _limitsList) { state, appList, icons, limitsList ->
            state.copy(
                appList = appList,
                appIcons = icons,
                limitsList = limitsList,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            LimitsState()
        )

    private fun checkUsageAccessPermission() {
        viewModelScope.launch {
            _limitsUseCase.isUsageAccessPermissionGranted().let {
                _isUsageAccessPermissionGranted.value = it
            }
        }
    }

    private fun checkSystemAlertWindowPermission() {
        viewModelScope.launch {
            _limitsUseCase.isSystemAlertPermissionGranted().let {
                _isSystemAlertWindowPermissionGranted.value = it
            }
        }
    }

    private fun syncLimits() {
        viewModelScope.launch {
            _state.value.limitsList.forEach { limit ->
                _limitsUseCase.upsertLimit(limit)
            }
        }
    }

    private fun mapLimitsToSelectedApplicationsToLimit() {
        viewModelScope.launch {
            var selectedApplicationsToLimit = mapOf<String, Int>()
            selectedApplicationsToLimit = state.value.limitsList.associate { limit ->
                limit.applicationPackageName to limit.timeLimit
            }

            _state.update {
                it.copy(
                    selectedApplicationsToLimit = selectedApplicationsToLimit
                )
            }
        }

    }

    init {
        checkUsageAccessPermission()
        checkSystemAlertWindowPermission()

        if (_isUsageAccessPermissionGranted.value && _isSystemAlertWindowPermissionGranted.value) {
            _limitsUseCase.registerTimeLimitService()
        }

        _applicationsUseCase.getApplicationsFromDatabase().onEach { appsList ->
            _appsList.update {
                appsList
            }
            _icons.update {
                _applicationsUseCase.getApplicationsIcons(appsList)
            }
        }.launchIn(viewModelScope)

        val resetLimitsIntent = IntentFilter("RESET_LIMITS")
        ContextCompat.registerReceiver(
            context,
            resetLimitsReceiver,
            resetLimitsIntent,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun onEvent(event: LimitsEvent) {
        when (event) {
            LimitsEvent.CheckUsageAccessPermission -> {
                checkUsageAccessPermission()
            }

            LimitsEvent.CheckSystemAlertWindowPermission -> {
                checkSystemAlertWindowPermission()
            }

            LimitsEvent.RegisterTimeLimitService -> {
                _limitsUseCase.registerTimeLimitService()
            }

            LimitsEvent.ShowApplicationsList -> {
                _state.update {
                    it.copy(
                        showApplicationsList = true
                    )
                }
                mapLimitsToSelectedApplicationsToLimit()
            }

            is LimitsEvent.AddApplicationToLimit -> {
                val timeIfLimitExist = state.value.limitsList.find {
                    it.applicationPackageName == event.packageName
                }?.timeLimit ?: 0

                val newSelectedApplicationsToLimit =
                    _state.value.selectedApplicationsToLimit.toMutableMap()

                newSelectedApplicationsToLimit.put(
                    event.packageName,
                    timeIfLimitExist
                )

                _state.update {
                    it.copy(
                        selectedApplicationsToLimit = newSelectedApplicationsToLimit.toMap()
                    )
                }

            }

            is LimitsEvent.EditApplicationToLimit -> {
                val newSelectedApplicationsToLimit =
                    _state.value.selectedApplicationsToLimit.toMutableMap()
                if (event.limitTime <= 300) {
                    newSelectedApplicationsToLimit[event.packageName] = event.limitTime
                    _state.update {
                        it.copy(
                            selectedApplicationsToLimit = newSelectedApplicationsToLimit.toMap()
                        )
                    }
                }
            }

            is LimitsEvent.RemoveApplicationToLimit -> {
                val newSelectedApplicationsToLimit =
                    _state.value.selectedApplicationsToLimit.toMutableMap()
                newSelectedApplicationsToLimit.remove(event.packageName)
                _state.update {
                    it.copy(
                        selectedApplicationsToLimit = newSelectedApplicationsToLimit.toMap()
                    )
                }

            }

            LimitsEvent.SaveApplicationLimits -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showApplicationsList = false
                        )
                    }

                    _state.value.selectedApplicationsToLimit.forEach { appLimit ->
                        if (appLimit.value != 0) {
                            val id =
                                state.value.limitsList.find { it.applicationPackageName == appLimit.key }?.id
                            val limit = LimitsModel(
                                id = id,
                                applicationPackageName = appLimit.key,
                                dateCreated = formatMillisecondsToSQLiteDate(System.currentTimeMillis()),
                                timeLimit = appLimit.value,
                                usedTime = 0
                            )
                            _limitsUseCase.upsertLimit(limit)
                        }
                    }

                    state.value.limitsList.forEach { limit ->
                        if (limit.applicationPackageName !in state.value.selectedApplicationsToLimit.keys) {
                            _limitsUseCase.deleteLimit(limit.id!!)
                        }
                    }
                }
            }

            LimitsEvent.SyncLimits -> {
                syncLimits()
            }
        }
    }
}
