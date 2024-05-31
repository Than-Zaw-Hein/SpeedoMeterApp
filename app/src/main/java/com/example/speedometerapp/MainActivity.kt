package com.example.speedometerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speedometerapp.screen.ElectricityConverter.ElectricityConverterScreen
import com.example.speedometerapp.screen.LengthCalculator.LengthCalculatorScreen
import com.example.speedometerapp.screen.MaterialColorPicker.MaterialColorPickerScreen
import com.example.speedometerapp.ui.theme.SpeedometerAppTheme
import com.example.speedometerapp.screen.SpeedoMeter.SpeedometerApp
import com.example.speedometerapp.screen.SpeedoMeter.speedCardModifier
import com.example.speedometerapp.ui.composable.AppImage
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedometerAppTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentScreen by rememberSaveable { mutableStateOf(Screen.Dashboard) }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                currentScreen.text, style = MaterialTheme.typography.headlineLarge
            )
        })
    }) { innerPadding ->
        MyNavHost(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            currentScreen = it
        }
    }
}

@Composable
fun DashBoardScreen(
    onClick: (Screen) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(Screen.entries.filter { it != Screen.Dashboard }) { screen ->
            DisplayTile(
                image = screen.image, text = screen.text
            ) {
                onClick(screen)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MyNavHost(modifier: Modifier, onCurrentScreenChange: (Screen) -> Unit) {
//    SharedTransitionLayout(modifier = modifier) {
//
//    }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DashboardRoute
    ) {
        composable<DashboardRoute> {
            onCurrentScreenChange(Screen.Dashboard)
            DashBoardScreen { screen ->
//                navController.navigate(screen)
            }
        }
        composable<SpeedometerRoute> {
            onCurrentScreenChange(Screen.Speedometer)
            SpeedometerApp(modifier = Modifier.fillMaxSize()) {
                navController.navigateUp()
            }
        }
        composable<LengthConverterRoute> {
            onCurrentScreenChange(Screen.LengthConverter)
            LengthCalculatorScreen(modifier = Modifier.fillMaxSize()) {
                navController.navigateUp()
            }
        }
        composable<ElectricityConverterRoute> {
            onCurrentScreenChange(Screen.ElectricityConverter)
            ElectricityConverterScreen() {
                navController.navigateUp()
            }
        }
        composable<MaterialColorPickerRoute> {
            onCurrentScreenChange(Screen.MaterialColorPicker)
            MaterialColorPickerScreen() {
                navController.navigateUp()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    SpeedometerAppTheme {
        MainScreen()
    }
}

@Composable
private fun DisplayTile(
    @DrawableRes image: Int,
    text: String,
    isGrid: Boolean = false,
    onClick: () -> Unit,
) {
    val gridModifier = if (isGrid) {
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    } else {
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    }

    ElevatedCard(
        modifier = gridModifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
    ) {
        AnimatedContent(isGrid, label = "IsGrid Content") {
            if (!it) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    AppImage(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp), image = image
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = text)
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    AppImage(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp), image = image
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = text)
                }
            }
        }
    }
}

enum class Screen(@DrawableRes open val image: Int, open val text: String) {
    Dashboard(image = 0, text = "Utility Software"),
    Speedometer(
        image = R.drawable.speedo_meter, text = "Speedometer",
    ),
    LengthConverter(
        image = R.drawable.length_converter,
        text = "Length Converter",
    ),
    MaterialColorPicker(
        image = R.drawable.material_color_picker,
        text = "Material Color Picker",
    ),
    ElectricityConverter(
        image = R.drawable.electricity_converter,
        text = "Electricity Converter",
    )
}
