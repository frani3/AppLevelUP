package com.applevelup.levepupgamerapp.presentation.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

@Composable
fun ProductImage(
    imageRes: Int?,
    imageUrl: String?,
    imageUri: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    when {
        !imageUri.isNullOrBlank() -> {
            AsyncImage(
                modifier = modifier,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Uri.parse(imageUri))
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = contentScale
            )
        }
        !imageUrl.isNullOrBlank() -> {
            AsyncImage(
                modifier = modifier,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = contentScale
            )
        }
        imageRes != null -> {
            Image(
                modifier = modifier,
                painter = painterResource(id = imageRes),
                contentDescription = contentDescription,
                contentScale = contentScale
            )
        }
        else -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.DarkGray.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = contentDescription,
                    tint = PrimaryPurple
                )
            }
        }
    }
}
