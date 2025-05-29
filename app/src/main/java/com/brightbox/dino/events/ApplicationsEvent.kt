package com.brightbox.dino.events

sealed class ApplicationsEvent {
    data class AppUninstalled(val packageName: String) : ApplicationsEvent()
    data class AppInstalled(val packageName: String) : ApplicationsEvent()
}