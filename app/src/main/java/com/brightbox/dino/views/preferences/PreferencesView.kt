package com.brightbox.dino.views.preferences

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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.constants.LANGUAGES
import com.brightbox.dino.constants.SearchEnginesEnum
import com.brightbox.dino.events.preferences.GeneralPreferencesEvent
import com.brightbox.dino.navigation.ApplicationsLimitRoute
import com.brightbox.dino.navigation.LocalNavController
import com.brightbox.dino.viewmodel.preferences.PreferencesViewModel
import com.brightbox.dino.views.common.DropdownComponent
import com.brightbox.dino.views.preferences.components.ToggleComponent
import com.brightbox.dino.views.theme.LocalSpacing

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
                    .padding(spacing.spaceLarge)
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
                ) {
                    // App language
//                    DropdownComponent(
//                        modifier = Modifier.fillMaxWidth(0.5f),
//                        text = "App language",
//                        textStyle = MaterialTheme.typography.bodyLarge,
//                        textFieldLabel = "",
//                        contentColor = MaterialTheme.colorScheme.onBackground,
//                        value = LANGUAGES[state.value.appLanguage].toString(),
//                        expanded = languageDropdownExpanded.value,
//                        setExpanded = {
//                            languageDropdownExpanded.value = !languageDropdownExpanded.value
//                        },
//                        items = LANGUAGES.values.toList(),
//                        onItemClick = {
//                            preferencesViewModel.onEvent(
//                                GeneralPreferencesEvent.SetAppLanguage(
//                                    LANGUAGES.keys.toList()[it].toString()
//                                )
//                            )
//                        },
//                    )

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