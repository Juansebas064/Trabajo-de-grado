package com.brightbox.hourglass.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.hourglass.model.ApplicationsModel
import com.brightbox.hourglass.states.ApplicationsState
import com.brightbox.hourglass.usecases.ApplicationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val _applicationsUseCase: ApplicationsUseCase
) : ViewModel() {

    // States
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val appsList = _applicationsUseCase.getApplicationsFromDatabase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredAppList = combine(
        appsList,
        _searchText
    ) { appsList, searchText ->
        val filteredApplications = if (searchText.isBlank()) {
            appsList
        } else {
            appsList.filter { it.name.contains(searchText, ignoreCase = true) }
        }
        ApplicationsState(
            applications = filteredApplications,
            searchText = searchText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApplicationsState())


    private val _appShowingOptions = MutableStateFlow("none")

    val appShowingOptions = _appShowingOptions.asStateFlow()

    private val _isKeyboardOpened = MutableStateFlow(false)
    val isKeyboardOpened = _isKeyboardOpened.asStateFlow()


    suspend fun onSearchTextChange(searchText: String = "") {   // searchText will be "" if no argument is passed
        Log.d("AppsViewModel", "onSearchTextChange: searchText = $searchText")
        _searchText.value = searchText
        withContext(Dispatchers.IO) {
            _applicationsUseCase.queryInstalledApplicationsToDatabase()
        }
    }

    fun openApp(packageName: String) {
        _applicationsUseCase.openApp(packageName)
        _searchText.value = ""
    }

    fun openApp(intent: Intent) {
        _applicationsUseCase.openApp(intent)
    }

    fun openFirstApp() {
        if (appsList.value.isNotEmpty()) {
            if (filteredAppList.value.applications.isNotEmpty()) {
                _applicationsUseCase.openApp(filteredAppList.value.applications.first().packageName)
                _searchText.value = ""    // Clear searchText
            } else {
                searchOnInternet()
            }
        }
    }

    fun setAppShowingOptions(packageName: String) {
        _appShowingOptions.value = packageName
    }

    fun toggleAppPinnedState(app: ApplicationsModel) {
        viewModelScope.launch {
            _applicationsUseCase.toggleAppPinnedState(app)
        }
    }

    fun uninstallApp(packageName: String) {
        viewModelScope.launch {
            _applicationsUseCase.uninstallApp(packageName)
        }
    }

    fun openAppInfo(app: ApplicationsModel) {
        _applicationsUseCase.openAppInfo(app.packageName)
    }

    fun setKeyboardState(keyboardState: Boolean) {
        _isKeyboardOpened.value = keyboardState // Clear searchText
    }

    fun searchOnInternet() {
        _applicationsUseCase.searchOnInternet(_searchText.value)
    }
}