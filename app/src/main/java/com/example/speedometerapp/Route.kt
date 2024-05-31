package com.example.speedometerapp
import kotlinx.serialization.Serializable


@Serializable
data class DashboardRoute(val name: String = "Dashboard")

@Serializable
data class SpeedometerRoute(
    val name: String = "Speedometer"
)

@Serializable
data class LengthConverterRoute(
    val name: String = "LengthConverter"
)

@Serializable
data class MaterialColorPickerRoute(
    val name: String = "MaterialColorPicker"
)

@Serializable
data class ElectricityConverterRoute(
    val name: String = "ElectricityConverter"
)
