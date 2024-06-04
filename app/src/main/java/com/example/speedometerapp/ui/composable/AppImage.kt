package com.example.speedometerapp.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppImage(
    modifier: Modifier = Modifier, @DrawableRes image: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    with(sharedTransitionScope) {
        ElevatedCard(modifier.sharedElement(
            state = rememberSharedContentState(key = "image/$image"),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ ->
                tween(durationMillis = 1000)
            }
        )) {
            Image(
                painter = painterResource(image),
                contentDescription = "Length Converter Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()

            )
        }
    }
}