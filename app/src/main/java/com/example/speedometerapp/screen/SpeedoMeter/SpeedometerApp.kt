package com.example.speedometerapp.screen.SpeedoMeter

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speedometerapp.Speed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerApp(modifier: Modifier, onBackPress: () -> Unit) {

    BackHandler { onBackPress() }
    val coroutineScope = rememberCoroutineScope()

    val viewModel = remember { SpeedometerViewModel() }
    var speed by remember { mutableIntStateOf(100) }
    val animateSpeed by animateFloatAsState(speed.toFloat(), label = "Speed")
    var acceleration by remember { mutableIntStateOf(5) }
    var velocity by remember { mutableIntStateOf(10) }
    var isDriving by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf("") }
    val speedInMilesPerHour = viewModel.calculateMilesFromKilometersPerHour(speed.toDouble())


    LaunchedEffect(Unit) {
        while (true) {
            val currentTimeMillis = System.currentTimeMillis()
            val currentDate = Date(currentTimeMillis)
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            time = dateFormat.format(currentDate)
            delay(30 * 1000)
        }
    }
    LaunchedEffect(isDriving) {
        while (true) {
            delay(1000)
            if (isDriving) {
                velocity += acceleration
//                speed += (velocity + (acceleration * 0.1f)).toInt().coerceIn(0, 240)

                speed = velocity
                speed = (speed + 5).coerceIn(0, 240)
            } else {
                speed = (speed - 5).coerceAtLeast(0)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        SpeedometerDial(animateSpeed, time)
        Text(
            text = "Your speed is ${speed} km/h (${
                String.format(
                    Locale.US, "%.2f", speedInMilesPerHour
                )
            } mph)", fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
        SpeedControlPanel(
            selectedSpeed = getClosestSpeed(speed),
            onSpeedChange = { newSpeed -> speed = newSpeed.value },
            onStopClick = {
                speed = 0
                isDriving = false
            },
            onDriveClick = {
                isDriving = it
            },
            onBreakClick = {
                speed = (speed - 5).coerceAtLeast(0)
            },

            )
    }
}


@Composable
fun SpeedometerDial(
    speed: Float, time: String
) {
//    val distanceInMiles = viewModel.convertKilometersToMiles(distance.toDouble())
    val onSurfaceColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier.size(180.dp), contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2
            drawArc(
                color = onSurfaceColor,
                startAngle = 149.5f,
                sweepAngle = 241f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx())
            )
            drawCircle(
                color = Color.Red,
                radius = 16f,
                colorFilter = ColorFilter.tint(Color.Red),
                style = Fill
            )

            for (i in 0..24) {
                val angle = 150 + (i * 10)
                val x1 =
                    center.x + (radius - if (i % 2 == 0) 40 else 20) * cos(angle * (PI / 180)).toFloat()
                val y1 =
                    center.y + (radius - if (i % 2 == 0) 40 else 30) * sin(angle * (PI / 180)).toFloat()
                val x2 = center.x + radius * cos(angle * (PI / 180)).toFloat()
                val y2 = center.y + radius * sin(angle * (PI / 180)).toFloat()
                drawLine(
                    onSurfaceColor,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = if (i % 2 == 0) 2.dp.toPx() else 2.dp.toPx()
                )

                // Draw speed labels
                val labelRadius = radius - 65
                val labelX = center.x + labelRadius * cos(angle * (PI / 180)).toFloat()
                val labelY = center.y + labelRadius * sin(angle * (PI / 180)).toFloat()
                if (i % 2 == 0) {
                    drawContext.canvas.nativeCanvas.apply {
                        drawText("${i * 10}", labelX, labelY, android.graphics.Paint().apply {
                            color = onSurfaceColor.toArgb()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 24f
                        })
                    }
                }
            }
            val speedAngle = 150f + speed
            val speedX = center.x + (radius - 20) * cos(speedAngle * (PI / 180)).toFloat()
            val speedY = center.y + (radius - 20) * sin(speedAngle * (PI / 180)).toFloat()
            drawLine(
                color = Color.Red,
                start = center,
                end = Offset(speedX, speedY),
                strokeWidth = 4.dp.toPx()
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Km/h", fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = time, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpeedControlPanel(
    selectedSpeed: Speed,
    onSpeedChange: (Speed) -> Unit,
    onStopClick: () -> Unit,
    onBreakClick: () -> Unit,
    onDriveClick: (isDriving: Boolean) -> Unit,
) {
    val speeds = Speed.entries.filter { it != Speed.SPEED_0 }

    LazyColumn {
        item { CarActionContent(onDriveClick, onBreakClick, onStopClick) }
        item { Spacer(Modifier.height(12.dp)) }


        items(speeds.chunked(4)) { rowSpeeds ->
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowSpeeds.forEach { speed ->
                    ElevatedCard(
                        colors = if (selectedSpeed == speed) CardDefaults.cardColors(
                            containerColor = Color.Red
                        ) else CardDefaults.cardColors()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(75.dp)
                                .height(80.dp)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${speed.value}km",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onSpeedChange(speed) })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CarActionContent(
    onDriveClick: (Boolean) -> Unit, onBreakClick: () -> Unit, onStopClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPress = interactionSource.collectIsPressedAsState().value
        val currentDriveState by rememberUpdatedState(onDriveClick)

        LaunchedEffect(isPress) {
            currentDriveState.invoke(isPress)
        }
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(45.dp)
                .clip(CircleShape)
                .clickable(interactionSource = interactionSource,
                    indication = androidx.compose.material.ripple.rememberRipple(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    enabled = true,
                    onClick = {})
                .background(
                    if (interactionSource.collectIsPressedAsState().value) MaterialTheme.colorScheme.primary else Color.Gray
                )
                .padding(8.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "Drive", color = Color.White)
        }
        Button(onClick = onBreakClick) {
            Text(text = "Brake")
        }
        Button(onClick = onStopClick) {
            Text(text = "Stop")
        }
    }
}

fun Modifier.speedCardModifier(selectedSpeed: Speed, speed: Speed): Modifier = this.then(
    if (selectedSpeed == speed) Modifier.background(Color.Red) else Modifier
)

fun getClosestSpeed(currentSpeed: Int): Speed {
    val speeds = Speed.entries.map { it.value }
    val closestSpeedIndex = speeds.indexOfFirst { it >= currentSpeed }
    val closestSpeed = speeds[closestSpeedIndex]
    return Speed.entries[closestSpeedIndex]
}

class SpeedometerViewModel {
    fun convertKilometersToMiles(km: Double): Double {
        return km / 1.6
    }

    fun convertMilesToKilometers(miles: Double): Double {
        return miles * 1.6
    }

    fun calculateMilesFromKilometersPerHour(kmPerHour: Double): Double {
        return kmPerHour / 1.6
    }
}

@Preview(showBackground = true)
@Composable
fun previewSpeedoMeter() {
    SpeedometerApp(Modifier.fillMaxSize()) {}
}