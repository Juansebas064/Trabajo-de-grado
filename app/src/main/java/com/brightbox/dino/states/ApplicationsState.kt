package com.brightbox.dino.states

import com.brightbox.dino.model.ApplicationsModel

data class ApplicationsState (
    val applications: List<ApplicationsModel> = emptyList(),
    val searchText: String = ""
)