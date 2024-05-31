package com.example.speedometerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.speedometerapp.screen.LengthCalculator.LengthCalculatorScreen
import com.example.speedometerapp.ui.theme.SpeedometerAppTheme
import com.example.speedometerapp.screen.SpeedoMeter.SpeedometerApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedometerAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var currentScreen by rememberSaveable { mutableStateOf(Screen.Dashboard) }
                    AnimatedContent(
                        currentScreen,
                        Modifier.padding(innerPadding),
                        label = "Main Screen"
                    ) {
                        when (it) {
                            Screen.Dashboard -> {
                                LazyColumn {
                                    items(Screen.entries.filter { it != Screen.Dashboard }) { screen ->
                                        DisplayTile(
                                            image = screen.image,
                                            text = screen.text
                                        ) {
                                            currentScreen = screen
                                        }
                                    }
                                }
                            }

                            Screen.Speedometer -> SpeedometerApp(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                currentScreen = Screen.Dashboard
                            }

                            Screen.LengthConverter -> LengthCalculatorScreen(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                currentScreen = Screen.Dashboard
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayTile(
    @DrawableRes image: Int,
    text: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(4.dp),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }
    }
}

enum class Screen(@DrawableRes val image: Int, val text: String) {
    Dashboard(image = 0, text = "Dashboard"),
    Speedometer(image = R.drawable.speedo_meter, text = "Speedometer"),
    LengthConverter(image = R.drawable.length_converter, text = "Length Converter"),
}