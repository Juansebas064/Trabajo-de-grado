package com.brightbox.dino.views.home.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.viewmodel.ApplicationsViewModel
import com.brightbox.dino.views.theme.LocalSpacing
import kotlinx.coroutines.launch

@Composable
fun SearchBarView(
    applicationsViewModel: ApplicationsViewModel = hiltViewModel(),
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    modifier: Modifier,
) {
    val searchText by applicationsViewModel.searchText.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    Box(
        modifier = Modifier
            .padding(vertical = spacing.spaceMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                spacing.spaceSmall,
                Alignment.Start
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(spacing.spaceSmall)
                )
                .padding(spacing.spaceMedium - spacing.spaceExtraSmall)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
            )

            BasicTextField(
                value = searchText,
                onValueChange = {
                    coroutineScope.launch {
                        applicationsViewModel.onSearchTextChange(it)
                        applicationsViewModel.setAppShowingOptions("none")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Go,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        if (searchText.isNotEmpty()) {
                            applicationsViewModel.openFirstApp()
                            focusManager.clearFocus()
                        }
                    }
                ),
                textStyle = TextStyle.Default.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                decorationBox = { innerTextField ->
                    // Box para el text field
                    Box(
                        contentAlignment = AbsoluteAlignment.CenterLeft,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Search",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                        }
                        innerTextField()
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
//                .wrapContentWidth()

//                .width(IntrinsicSize.Min)
//                .onKeyEvent { event ->
//                    if (event.key.nativeKeyCode == KeyEvent.KEYCODE_BACK) {
//                        focusManager.clearFocus()
//                        true
//                    } else {
//                        false
//
//                    }
//                }
            )
        }
    }
}