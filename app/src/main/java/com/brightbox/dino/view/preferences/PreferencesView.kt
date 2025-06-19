package com.brightbox.dino.view.preferences

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.model.constants.FORM_LINK
import com.brightbox.dino.model.constants.SearchEnginesEnum
import com.brightbox.dino.events.preferences.GeneralPreferencesEvent
import com.brightbox.dino.viewmodel.preferences.PreferencesViewModel
import com.brightbox.dino.view.common.DropdownComponent
import com.brightbox.dino.view.common.RoundedSquareButtonComponent
import com.brightbox.dino.view.preferences.components.ToggleComponent
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun PreferencesView(
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val state = preferencesViewModel.state.collectAsState()
    val activity = LocalActivity.current
    val context = LocalContext.current

    var languageDropdownExpanded = remember { mutableStateOf(false) }
    var searchEngineDropdownExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        if (!state.value.isLoading) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(spacing.spaceMedium)
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = spacing.spaceExtraLarge)
                ) {
                    Text(
                        text = context.getString(R.string.settings),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        spacing.spaceMedium,
                        Alignment.Top
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(spacing.spaceMedium)
                ) {
                    // Show solid background
                    ToggleComponent(
                        text = context.getString(R.string.show_solid_background),
                        checked = state.value.solidBackground,
                        onCheckedChange = {
                            preferencesViewModel.onEvent(
                                GeneralPreferencesEvent.SetSolidBackground(it)
                            )
                        }
                    )

                    // Open keyboard automatically in app menu
                    ToggleComponent(
                        text = context.getString(R.string.open_keyboard_auto),
                        checked = state.value.openKeyboardInAppMenu,
                        onCheckedChange = {
                            preferencesViewModel.onEvent(
                                GeneralPreferencesEvent.SetOpenKeyboardInAppMenu(it)
                            )
                        }
                    )

                    // Show search on internet on app menu
                    ToggleComponent(
                        text = context.getString(R.string.show_search_on_internet),
                        checked = state.value.showSearchOnInternet,
                        onCheckedChange = {
                            preferencesViewModel.onEvent(
                                GeneralPreferencesEvent.SetShowSearchOnInternet(it)
                            )
                        }
                    )

                    // If enabled ^: Select search engine
                    AnimatedVisibility(
                        visible = state.value.showSearchOnInternet
                    ) {
                        DropdownComponent(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            text = context.getString(R.string.search_engine),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            textFieldLabel = "",
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            value = state.value.searchEngine,
                            expanded = searchEngineDropdownExpanded.value,
                            setExpanded = {
                                searchEngineDropdownExpanded.value =
                                    !searchEngineDropdownExpanded.value
                            },
                            items = SearchEnginesEnum.entries.map { it.engineName }.toList(),
                            onItemClick = {
                                preferencesViewModel.onEvent(
                                    GeneralPreferencesEvent.SetSearchEngine(
                                        SearchEnginesEnum.entries[it].engineName
                                    )
                                )
                            },
                        )
                    }

                    // Show essential shortcuts
                    ToggleComponent(
                        text = context.getString(R.string.show_essential_shortcuts),
                        checked = state.value.showEssentialShortcuts,
                        onCheckedChange = {
                            preferencesViewModel.onEvent(
                                GeneralPreferencesEvent.SetShowEssentialShortcuts(it)
                            )
                        }
                    )

                    // Set Dino as default launcher
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(spacing.spaceSmall))
                    ) {
                        Text(
                            text = context.getString(R.string.set_as_default_launcher),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .weight(1f)
                        )

                        RoundedSquareButtonComponent(
                            onClick = {
                                val goToHomeSettings = Intent(Settings.ACTION_HOME_SETTINGS)
                                context.startActivity(goToHomeSettings)
                            },
                            text = context.getString(R.string.go),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.inversePrimary,
                        )
                    }
                }


                // Go to form
                Button(
                    onClick = {
                        val goToFormIntent = Intent(ACTION_VIEW, FORM_LINK.toUri())
                        context.startActivity(goToFormIntent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(spacing.spaceSmall),
                    contentPadding = PaddingValues(spacing.spaceMedium),
                    modifier = Modifier

                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = context.getString(R.string.help_me_filling_this_form),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "Set application limit",
                            modifier = Modifier
                                .scale(0.7f)
                        )
                    }
                }
            }

            // Close settings
            IconButton(
                onClick = {
                    activity!!.finish()
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}