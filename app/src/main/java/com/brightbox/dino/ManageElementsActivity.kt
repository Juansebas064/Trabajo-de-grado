package com.brightbox.dino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.brightbox.dino.view.manage_elements.ManageTasksHabitsAndCategoriesView
import com.brightbox.dino.view.theme.DinoLauncherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageElementsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DinoLauncherTheme {
                ManageTasksHabitsAndCategoriesView()
            }
        }
    }
}