package com.brightbox.dino

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.brightbox.dino.views.manage_elements.ManageTasksHabitsAndCategoriesView
import com.brightbox.dino.views.theme.DinoLauncherTheme
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