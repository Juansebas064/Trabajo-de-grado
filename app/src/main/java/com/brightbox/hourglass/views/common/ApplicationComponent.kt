package com.brightbox.hourglass.views.common

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.brightbox.hourglass.model.ApplicationsModel
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun ApplicationComponent(
    modifier: Modifier = Modifier,
    application: ApplicationsModel,
    icon: Drawable? = null,
    showIcon: Boolean = false,
    textStyle: TextStyle,
    textColor: Color,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
        modifier = modifier
    ) {
        if (showIcon) {
            // Coil se encarga de cargar el Drawable de forma asíncrona y manejar el cacheo
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(icon)
                        .build()
                ),
                contentDescription = application.name,
//                contentScale = ContentScale.FillHeight,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = application.name,
            style = textStyle,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f, fill = false)
        )

    }
}