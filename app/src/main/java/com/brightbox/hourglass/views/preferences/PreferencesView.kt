package com.brightbox.hourglass.views.preferences

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.constants.LANGUAGES
import com.brightbox.hourglass.constants.SearchEnginesEnum
import com.brightbox.hourglass.events.preferences.GeneralPreferencesEvent
import com.brightbox.hourglass.viewmodel.preferences.PreferencesViewModel
import com.brightbox.hourglass.views.common.DropdownComponent
import com.brightbox.hourglass.views.preferences.components.ToggleComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun PreferencesView(
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val state = preferencesViewModel.state.collectAsState()
    val activity = LocalActivity.current

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
                    .padding(spacing.spaceLarge)
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = spacing.spaceExtraLarge)
                ) {
                    Text(
                        text = "Settings",
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
                ) {
                    // App language
                    DropdownComponent(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        text = "App language",
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textFieldLabel = "",
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        value = LANGUAGES[state.value.appLanguage].toString(),
                        expanded = languageDropdownExpanded.value,
                        setExpanded = {
                            languageDropdownExpanded.value = !languageDropdownExpanded.value
                        },
                        items = LANGUAGES.values.toList(),
                        onItemClick = {
                            preferencesViewModel.onEvent(
                                GeneralPreferencesEvent.SetAppLanguage(
                                    LANGUAGES.keys.toList()[it].toString()
                                )
                            )
                        },
                    )

                    // Show solid background
                    ToggleComponent(
                        text = "Show solid background",
                        checked = state.value.solidBackground,
                        onCheckedChange = {
                            preferencesViewModel.onEvent(
                                GeneralPreferencesEvent.SetSolidBackground(it)
                            )
                        }
                    )

                    // Open keyboard automatically in app menu
                    ToggleComponent(
                        text = "Open keyboard automatically in app menu",
                        checked = state.value.openKeyboardInAppMenu,
                        onCheckedChange = {
                            preferencesViewModel.onEvent(
                                GeneralPreferencesEvent.SetOpenKeyboardInAppMenu(it)
                            )
                        }
                    )

                    // Show search on internet on app menu
                    ToggleComponent(
                        text = "Show search on internet in app menu",
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
                            text = "Search engine",
                            textStyle = MaterialTheme.typography.bodyLarge,
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


                    // Turn on dnd when pomodoro is running


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