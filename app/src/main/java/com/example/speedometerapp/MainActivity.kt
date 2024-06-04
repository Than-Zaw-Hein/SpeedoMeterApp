package com.example.speedometerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speedometerapp.screen.electricityConverter.ElectricityConverterScreen
import com.example.speedometerapp.screen.lengthCalculator.LengthCalculatorScreen
import com.example.speedometerapp.screen.materialColorPicker.MaterialColorPickerScreen
import com.example.speedometerapp.ui.theme.SpeedometerAppTheme
import com.example.speedometerapp.screen.speedoMeter.SpeedometerApp
import com.example.speedometerapp.screen.volumnConverter.VolumeConverterScreen
import com.example.speedometerapp.ui.composable.AppImage

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen() {

    var currentScreen by rememberSaveable { mutableStateOf(Screen.Dashboard) }

    var isGrid by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        currentScreen.text, style = MaterialTheme.typography.headlineLarge
                    )
                },
                actions = {
                    if (currentScreen == Screen.Dashboard) {
                        IconButton(onClick = {
                            isGrid = !isGrid
                        }) {
                            if (isGrid) {
                                Icon(
                                    imageVector = Icons.Default.Menu, contentDescription = "LIST"
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.grid),
                                    contentDescription = "GRID",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        MyNavHost(
            Modifier.padding(innerPadding), isGrid
        ) {
            currentScreen = it
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DashBoardScreen(
    isGrid: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: (Screen) -> Unit,
) {
    if (!isGrid) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp
            )
        ) {
            items(Screen.entries.filter { it != Screen.Dashboard },
                key = { it.name }) { screen ->
                DisplayTile(
                    modifier = Modifier,
                    image = screen.image,
                    text = screen.text,
                    isGrid = isGrid,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                ) {
                    onClick(screen)
                }
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(Screen.entries.filter { it != Screen.Dashboard },
                key = { it.name }) { screen ->
                DisplayTile(
                    modifier = Modifier.animateItem(),
                    image = screen.image,
                    text = screen.text,
                    isGrid = isGrid,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                ) {
                    onClick(screen)
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MyNavHost(
    modifier: Modifier,
    isGrid: Boolean,
    onCurrentScreenChange: (Screen) -> Unit
) {
    SharedTransitionLayout(modifier) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,

            ) {
            composable<DashboardRoute> {
                onCurrentScreenChange(Screen.Dashboard)
                DashBoardScreen(isGrid, this@SharedTransitionLayout, this) { screen ->
                    if (!this@SharedTransitionLayout.isTransitionActive) {
                        navController.navigate(screen.mClass)
                    }
                }
            }
            composable<SpeedometerRoute> {
                onCurrentScreenChange(Screen.Speedometer)
                SpeedometerApp(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (!this@SharedTransitionLayout.isTransitionActive) {
                        navController.navigateUp()
                    }
                }
            }
            composable<LengthConverterRoute> {
                onCurrentScreenChange(Screen.LengthConverter)
                LengthCalculatorScreen(
                    modifier = Modifier.fillMaxSize(),
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                ) {
                    if (!this@SharedTransitionLayout.isTransitionActive) {
                        navController.navigateUp()
                    }
                }
            }
            composable<ElectricityConverterRoute> {
                onCurrentScreenChange(Screen.ElectricityConverter)
                ElectricityConverterScreen(
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                ) {
                    if (!this@SharedTransitionLayout.isTransitionActive) {
                        navController.navigateUp()
                    }
                }
            }
            composable<MaterialColorPickerRoute> {
                onCurrentScreenChange(Screen.MaterialColorPicker)
                MaterialColorPickerScreen(
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                ) {
                    if (!this@SharedTransitionLayout.isTransitionActive) {
                        navController.navigateUp()
                    }
                }
            }
            composable<VolumeConverterRoute> {
                onCurrentScreenChange(Screen.VolumeConverter)
                VolumeConverterScreen(
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                ) {
                    if (!this@SharedTransitionLayout.isTransitionActive) {
                        navController.navigateUp()
                    }
                }
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DisplayTile(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    text: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    isGrid: Boolean,
    onClick: () -> Unit,
) {

    ElevatedCard(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
    ) {
        if (!isGrid) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                AppImage(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                    image = image,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
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
                        .padding(8.dp),
                    image = image,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = text, textAlign = TextAlign.Center)
            }
        }
    }
}

enum class Screen(@DrawableRes open val image: Int, open val text: String, val mClass: AppRoute) {
    Dashboard(image = 0, text = "Utility Software", DashboardRoute), Speedometer(
        image = R.drawable.speedo_meter, text = "Speedometer", SpeedometerRoute()
    ),
    LengthConverter(
        image = R.drawable.length_converter, text = "Length Converter", LengthConverterRoute()
    ),
    MaterialColorPicker(
        image = R.drawable.material_color_picker,
        text = "Material Color Picker",
        MaterialColorPickerRoute()
    ),
    ElectricityConverter(
        image = R.drawable.electricity_converter,
        text = "Electricity Converter",
        ElectricityConverterRoute()
    ),
    VolumeConverter(
        image = R.drawable.volume_converter,
        text = "Volume Converter", VolumeConverterRoute()
    )

}
