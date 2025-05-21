package com.brightbox.hourglass.constants

enum class PrioritiesEnum(
    val priority: String,
    val value: Int
) {
    High(
        priority = "High",
        value = 1
    ),
    Medium(
        priority = "Medium",
        value = 2
    ),
    Low(
        priority = "Low",
        value = 3
    )
}

