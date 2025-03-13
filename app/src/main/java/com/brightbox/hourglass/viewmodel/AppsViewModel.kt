package com.brightbox.hourglass.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.hourglass.events.AppChangeEvent
import com.brightbox.hourglass.model.ApplicationModel
import com.brightbox.hourglass.states.ApplicationState
import com.brightbox.hourglass.usecases.AppUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.text.contains

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    // UseCases
    private val _appUseCase = AppUseCase(application)

    // States
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val appsList = combine(
        _appUseCase.getApplicationsFromDatabase(),
        _searchText
    ) { applicationsFromDatabase, searchText ->
        val filteredApplications = if (searchText.isBlank()) {
            applicationsFromDatabase
        } else {
            applicationsFromDatabase.filter { it.name.contains(searchText, ignoreCase = true) }
        }
        ApplicationState(
            applications = filteredApplications,
            searchText = searchText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApplicationState())

    private val _appShowingOptions = MutableStateFlow("none")

    val appShowingOptions = _appShowingOptions.asStateFlow()

    private val _isKeyboardOpened = MutableStateFlow(false)
    val isKeyboardOpened = _isKeyboardOpened.asStateFlow()


    // Functions

//    fun onEvent(event: AppChangeEvent) {
//        when (event) {
//            is AppChangeEvent.AppUninstalled -> {
//                Log.d("AppsViewModel", "onEvent: App uninstalled: ${event.packageName}")
//                viewModelScope.launch {
//                    _appUseCase.queryInstalledApplicationsToDatabase()
//                }
//        }
//            is AppChangeEvent.AppInstalled -> {
//                Log.d("AppsViewModel", "onEvent: App installed: ${event.packageName}")
//            }
//        }
//    }

    fun onSearchTextChange(searchText: String = "") {   // searchText will be "" if no argument is passed
        Log.d("AppsViewModel", "onSearchTextChange: searchText = $searchText")
        _searchText.value = searchText
        CoroutineScope(Dispatchers.IO).launch {
            _appUseCase.queryInstalledApplicationsToDatabase()
        }
    }

    fun openApp(packageName: String) {
        _appUseCase.openApp(packageName)
        _searchText.value = ""
    }

    fun openFirstApp() {
        if (appsList.value.applications.isNotEmpty()) {
            _appUseCase.openApp(appsList.value.applications.first().packageName)
            _searchText.value = ""    // Clear searchText
        }
    }

    fun setAppShowingOptions(packageName: String) {
        _appShowingOptions.value = packageName
    }

    fun toggleAppPinnedState(app: ApplicationModel) {
        viewModelScope.launch {
            _appUseCase.toggleAppPinnedState(app)
        }
    }

    fun uninstallApp(packageName: String) {
        viewModelScope.launch {
            _appUseCase.uninstallApp(packageName)
        }
    }

    fun openAppInfo(app: ApplicationModel) {
        _appUseCase.openAppInfo(app.packageName)
    }

    fun setKeyboardState(keyboardState: Boolean) {
        _isKeyboardOpened.value = keyboardState // Clear searchText
    }
}