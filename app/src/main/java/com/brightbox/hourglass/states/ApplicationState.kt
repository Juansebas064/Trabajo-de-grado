package com.brightbox.hourglass.states

import com.brightbox.hourglass.model.ApplicationModel

data class ApplicationState (
    val applications: List<ApplicationModel> = emptyList(),
    val searchText: String = ""
)