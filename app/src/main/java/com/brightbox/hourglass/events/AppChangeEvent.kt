package com.brightbox.hourglass.events

sealed class AppChangeEvent {
    data class AppUninstalled(val packageName: String) : AppChangeEvent()
    data class AppInstalled(val packageName: String) : AppChangeEvent()
}