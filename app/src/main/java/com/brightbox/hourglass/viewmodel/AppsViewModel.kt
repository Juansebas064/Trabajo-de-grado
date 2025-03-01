package com.brightbox.hourglass.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.hourglass.model.ApplicationModel
import com.brightbox.hourglass.usecases.AppUseCase
import com.brightbox.hourglass.usecases.AppsMenuUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        @Volatile
        private var INSTANCE: AppsViewModel? = null
        fun getInstance(application: Application): AppsViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppsViewModel(application).also { INSTANCE = it }
            }
        }
    }

    // UseCases
    private val _appsMenuUseCase = AppsMenuUseCase(application)
    private val _appUseCase = AppUseCase(application)

    // States
    private val _appsList = MutableStateFlow(emptyList<ApplicationModel>())
    val appsList = _appsList.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _appShowingOptions = MutableStateFlow("none")
    val appShowingOptions = _appShowingOptions.asStateFlow()

    private val _isKeyboardOpened = MutableStateFlow(false)
    val isKeyboardOpened = _isKeyboardOpened.asStateFlow()

    init {
        queryInstalledApps()
        getApps()
        Log.d("AppsViewModel", _appsList.value.toString())
    }

    // Functions

    fun queryInstalledApps() {
        _appsMenuUseCase.queryInstalledApps()
    }

    fun getApps() {
        viewModelScope.launch {
            _appsList.value = _appsMenuUseCase.getApps(searchText.value)
//            _appShowingOptions.value = "none"
        }
    }

    fun onSearchTextChange(searchText: String = "") {   // searchText will be "" if no argument is passed
        _searchText.value = searchText
        getApps()
    }

    fun openApp(packageName: String) {
        _appUseCase.openApp(packageName)
        _searchText.value = ""
        getApps()
    }

    fun openFirstApp() {
        if (_appsList.value.isNotEmpty()) {
            _appUseCase.openApp(_appsList.value.first().packageName)
            _searchText.value = ""    // Clear searchText
        }
    }

    fun setAppShowingOptions(packageName: String) {
        _appShowingOptions.value = packageName
    }

    fun toggleAppPinnedState(app: ApplicationModel) {
        viewModelScope.launch {
            _appUseCase.toggleAppPinnedState(app)
//            getApps()
        }
    }

    fun uninstallApp(app: ApplicationModel) {
        viewModelScope.launch {
            _appUseCase.uninstallApp(app)
//            Log.d("AppsViewModel", "uninstallApp: $result")
        }
    }

    fun openAppInfo(app: ApplicationModel) {
        _appUseCase.openAppInfo(app.packageName)
    }

    fun setKeyboardState(keyboardState: Boolean) {
        _isKeyboardOpened.value = keyboardState // Clear searchText
    }
}