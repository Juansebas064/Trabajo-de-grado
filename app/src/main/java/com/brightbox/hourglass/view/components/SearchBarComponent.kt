package com.brightbox.hourglass.view.components

import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onFocusedBoundsChanged
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brightbox.hourglass.viewmodel.AppsViewModel

@Composable
fun SearchBarComponent(
    appsViewModel: AppsViewModel,
    searchText: String,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    isKeyboardOpen: Boolean,
    modifier: Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { appsViewModel.onSearchTextChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if (searchText.isNotEmpty()) {
                        appsViewModel.openFirstApp()
                        focusManager.clearFocus()
                    }
                }
            ),
            textStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    if (searchText.isEmpty()) {
                        Text(
                            text = "Search",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    innerTextField()
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(15.dp)
                )
                .height(50.dp)
                .focusRequester(focusRequester)
                .onKeyEvent { event ->
                    if (event.key.nativeKeyCode == KeyEvent.KEYCODE_BACK) {
                        focusManager.clearFocus()
                        true
                    } else {
                        false

                    }
                }
        )
    }
}