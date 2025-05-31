package com.brightbox.dino.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_PACKAGE_ADDED
import android.content.Intent.ACTION_PACKAGE_REMOVED
import android.content.IntentFilter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.dino.R
import com.brightbox.dino.constants.SearchEnginesEnum
import com.brightbox.dino.model.ApplicationsModel
import com.brightbox.dino.states.ApplicationsState
import com.brightbox.dino.usecases.ApplicationsUseCase
import com.brightbox.dino.usecases.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val _applicationsUseCase: ApplicationsUseCase,
    private val _preferencesUseCase: PreferencesUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val appChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            queryInstalledApplicationsToDatabase()
        }
    }

    fun queryInstalledApplicationsToDatabase() {
        viewModelScope.launch {
            _applicationsUseCase.queryInstalledApplicationsToDatabase()
        }
    }

    init {
        queryInstalledApplicationsToDatabase()

        val appChangeIntentFilter = IntentFilter().apply {
            addAction(ACTION_PACKAGE_ADDED)
            addAction(ACTION_PACKAGE_REMOVED)
            addAction("QUERY_APPLICATIONS_TO_DATABASE")
            addDataScheme("package") // Necesario para ACTION_PACKAGE_REMOVED, _ADDED, _REPLACED
        }
        ContextCompat.registerReceiver(
            context,
            appChangeReceiver,
            appChangeIntentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

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
            // Normalizamos a minúsculas para no repetir lowercase() en cada comparación

            appsList
                // 1) Filtramos solo las que contienen el texto
                .filter { it.name.lowercase().contains(searchText.lowercase()) }
                // 2) Las ordenamos con dos claves:
                //    a) Las que NO empiezan con lowerSearch van después (true> false)
                //    b) Dentro de cada grupo, por índice de la subcadena (más bajo = más parecido)
                .sortedWith(
                    compareBy(
                        { !it.name.lowercase().startsWith(searchText, ignoreCase = true) },
                        { it.name.lowercase().indexOf(searchText, ignoreCase = true) }
                    ))
        }
        ApplicationsState(
            applications = filteredApplications,
            searchText = searchText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApplicationsState())


    private val _appShowingOptions = MutableStateFlow("none")

    val appShowingOptions = _appShowingOptions.asStateFlow()

    fun onSearchTextChange(searchText: String = "") {
        _searchText.value = searchText
    }

    fun openApp(app: ApplicationsModel) {
        if (!app.limitTimeReached) {
            _applicationsUseCase.openApp(app)
            _searchText.value = ""
        } else {
            Toast.makeText(
                context,
                "${context.getString(R.string.time_limit_for)} ${app.name} ${context.getString(R.string.reached)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun openApp(intent: Intent) {
        _applicationsUseCase.openApp(intent)
    }

    fun openFirstApp() {
        if (appsList.value.isNotEmpty()) {
            if (filteredAppList.value.applications.isNotEmpty()) {
                openApp(filteredAppList.value.applications.first())
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

    fun searchOnInternet() {
        viewModelScope.launch {
            _preferencesUseCase.getPreferences().collectLatest { preferences ->
                if (preferences.showSearchOnInternet) {
                    val searchEngineEnum =
                        SearchEnginesEnum.entries.find { it.engineName == preferences.searchEngine }
                    _applicationsUseCase.searchOnInternet(_searchText.value, searchEngineEnum!!)
                }
            }
        }
    }
}