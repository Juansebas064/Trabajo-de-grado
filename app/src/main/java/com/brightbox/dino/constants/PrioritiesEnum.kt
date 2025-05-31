package com.brightbox.dino.constants

import com.brightbox.dino.R

enum class PrioritiesEnum(
    val priority: String,
    val stringResource: Int,
    val value: Int
) {
    High(
        priority = "High",
        stringResource = R.string.priority_high,
        value = 1
    ),
    Medium(
        priority = "Medium",
        stringResource = R.string.priority_medium,
        value = 2
    ),
    Low(
        priority = "Low",
        stringResource = R.string.priority_low,
        value = 3
    )
}