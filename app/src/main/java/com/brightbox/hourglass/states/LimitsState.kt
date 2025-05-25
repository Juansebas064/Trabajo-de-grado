package com.brightbox.hourglass.states

import android.graphics.drawable.Drawable
import com.brightbox.hourglass.model.ApplicationsModel
import com.brightbox.hourglass.model.LimitsModel

data class LimitsState(
    val appList: List<ApplicationsModel> = emptyList(),
    val appIcons: Map<String, Drawable> = mapOf(),
    val limitsList: List<LimitsModel> = emptyList(),
    val selectedApplicationsToLimit: Map<String, Int> = mapOf(), //Map of packageNames and limitTimes

    val limitId: Int? = null,
    val limitPackageName: String = "",
    val limitTime: Int = 0,
    val limitUsedTime: Int = 0,

    val showApplicationsList: Boolean = false
)