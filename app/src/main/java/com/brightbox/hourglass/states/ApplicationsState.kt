package com.brightbox.hourglass.states

import com.brightbox.hourglass.model.ApplicationsModel

data class ApplicationsState (
    val applications: List<ApplicationsModel> = emptyList(),
    val searchText: String = ""
)