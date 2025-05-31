package com.brightbox.dino.events

sealed interface LimitsEvent {
    data object CheckUsageAccessPermission: LimitsEvent
    data object CheckSystemAlertWindowPermission: LimitsEvent
    data object RegisterTimeLimitService: LimitsEvent

    data class AddApplicationToLimit(val packageName: String): LimitsEvent
    data class EditApplicationToLimit(val packageName: String, val limitTime: Int): LimitsEvent
    data class RemoveApplicationToLimit(val packageName: String): LimitsEvent

    data object ShowApplicationsList: LimitsEvent
    data object SaveApplicationLimits: LimitsEvent
    data object SyncLimits: LimitsEvent
}