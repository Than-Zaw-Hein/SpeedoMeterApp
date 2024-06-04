package com.example.speedometerapp.screen.materialColorPicker

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.speedometerapp.R
import com.example.speedometerapp.ui.composable.AppImage
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MaterialColorPickerScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackPress: () -> Unit
) {
    BackHandler { onBackPress() }
    ColorPicker(
        modifier = Modifier.fillMaxSize(), sharedTransitionScope, animatedVisibilityScope,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ColorPicker(
    modifier: Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    var alpha by rememberSaveable { mutableFloatStateOf(1f) }
    var red by rememberSaveable { mutableFloatStateOf(0f) }
    var green by rememberSaveable { mutableFloatStateOf(0f) }
    var blue by rememberSaveable { mutableFloatStateOf(0f) }

    val hexColor = toHexColor(alpha, red, green, blue)
    val color = Color(red, green, blue, alpha)
    LazyColumn(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AppImage(
                modifier = Modifier.size(100.dp),
                R.drawable.material_color_picker,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )
        }
        item {
            // Display the current color in a Box with a MaterialTheme shape
            OutlinedTextField(
                singleLine = true,
                value = "Selected Hex Color: $hexColor",
                onValueChange = {},
                textStyle = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            )
        }
        item {
            Box(
                modifier = Modifier
                    .padding(10.dp, 0.dp)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(color),

                ) {}
        }
        itemsIndexed(
            listOf(
                "A" to alpha, "R" to red, "G" to green, "B" to blue
            )
        ) { index, (label, value) ->
            ColorSlider(
                label, value, when (label) {
                    "A" -> color.copy(1f)
                    "R" -> Color.Red
                    "G" -> Color.Green
                    "B" -> Color.Blue
                    else -> color
                }
            ) {
                when (label) {
                    "A" -> alpha = it
                    "R" -> red = it
                    "G" -> green = it
                    "B" -> blue = it
                }
            }
        }
    }
}

@Composable
private fun ColorSlider(
    label: String,
    valueState: Float,
    color: Color,
    onValueChange: (Float) -> Unit,
) {
    /**
     * Displays a slider for adjusting the given [valueState] associated with the provided [label].
     * The slider's active track color is set to [color].
     */
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = label)
        Slider(
            value = valueState, onValueChange = onValueChange, colors = SliderDefaults.colors(
                activeTrackColor = color
            ), modifier = Modifier.weight(1f)
        )
        Text(
            text = valueState.toColorInt().toString(),
            modifier = Modifier.width(25.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun Float.toColorInt(): Int = (this * 255 + 0.5f).toInt()

// Extension function to convert ARGB to hex string
fun toHexColor(alpha: Float, red: Float, green: Float, blue: Float): String {
    val a = (alpha * 255).roundToInt()
    val r = (red * 255).roundToInt()
    val g = (green * 255).roundToInt()
    val b = (blue * 255).roundToInt()
    return String.format("#%02X%02X%02X%02X", a, r, g, b)
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun previewSpeedoMeter() {
//    SharedTransitionLayout() { MaterialColorPickerScreen(this, { }) }
}