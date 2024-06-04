package com.example.speedometerapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.serialization.Serializable

//
//@Serializable
//sealed class AppRoute(val route: String) {
//    data object Dashboard : AppRoute("DashboardRoute")
//    data class Speedometer(val name: String = "Speedometer") : AppRoute("SpeedometerRoute")
//    data class LengthConverter(val name: String = "LengthConverter") :
//        AppRoute("LengthConverterRoute")
//
//    data class MaterialColorPicker(val name: String = "MaterialColorPicker") :
//        AppRoute("MaterialColorPickerRoute")
//
//    data class ElectricityConverter(val name: String = "ElectricityConverter") :
//        AppRoute("ElectricityConverterRoute")
//}

interface AppRoute

@Serializable
data object DashboardRoute : AppRoute

@Serializable
data class SpeedometerRoute(
    val name: String = "Speedometer"
) : AppRoute

@Serializable
data class LengthConverterRoute(
    val name: String = "LengthConverter"
) : AppRoute

@Serializable
data class MaterialColorPickerRoute(
    val name: String = "MaterialColorPicker"
) : AppRoute

@Serializable
data class ElectricityConverterRoute(
    val name: String = "ElectricityConverter"
) : AppRoute


@Serializable
data class VolumeConverterRoute(
    val name: String = "VolumeConverter"
) : AppRoute

@Composable
fun NavBackStackEntry?.currentRoute(): AppRoute? {
    return when (this?.destination?.route) {
        DashboardRoute.toString() -> DashboardRoute
        SpeedometerRoute().toString() -> SpeedometerRoute()
        LengthConverterRoute().toString() -> LengthConverterRoute()
        MaterialColorPickerRoute().toString() -> MaterialColorPickerRoute()
        ElectricityConverterRoute().toString() -> ElectricityConverterRoute()
        VolumeConverterRoute().toString() -> VolumeConverterRoute()
        else -> DashboardRoute
    }
}