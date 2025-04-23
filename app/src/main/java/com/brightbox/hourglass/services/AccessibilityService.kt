package com.brightbox.hourglass.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class AccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    fun openShade() =
        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)  // acción 4
}