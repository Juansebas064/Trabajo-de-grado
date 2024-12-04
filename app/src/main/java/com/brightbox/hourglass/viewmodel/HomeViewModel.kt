package com.brightbox.hourglass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.brightbox.hourglass.usecases.AppUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // UseCases
    private val _appUseCase = AppUseCase(application)


}