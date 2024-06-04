package com.example.speedometerapp.screen.electricityConverter

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.speedometerapp.R
import com.example.speedometerapp.ui.composable.AppImage

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ElectricityConverterScreen(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackPress: () -> Unit
) {
    BackHandler() {
        if (!sharedTransitionScope.isTransitionActive) {
            onBackPress()
        }
    }
    var inputValue by remember { mutableStateOf("1") }
    var inputUnit by remember { mutableStateOf(ChargeUnit.Coulomb) }
    var outputUnit by remember { mutableStateOf(ChargeUnit.Kilocoulomb) }
    val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
    val outputValueDouble = convertCharge(inputValueDouble, inputUnit, outputUnit)

    val formulaMessage = try {
        val formula = convertCharge(1.0, inputUnit, outputUnit)
        "1 ${inputUnit.name.lowercase()} = $formula ${outputUnit.name.lowercase()}"
    } catch (e: NumberFormatException) {
        "Invalid input value"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        AppImage(
            modifier = Modifier.size(100.dp),
            image = R.drawable.electricity_converter,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = inputValue,
                singleLine = true,
                maxLines = 1,
                onValueChange = { inputValue = it },
                label = { Text(text = "Input Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = outputValueDouble.toString(),
                onValueChange = {},singleLine = true,
                maxLines = 1,
                label = { Text(text = "Output Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            UnitSelector(modifier = Modifier.weight(1f),
                unit = inputUnit.displayName,
                onUnitChange = { newUnit ->
                    inputUnit = ChargeUnit.values().first { it.displayName == newUnit }
                })
            Text("=", style = MaterialTheme.typography.headlineLarge)
            UnitSelector(modifier = Modifier.weight(1f),
                unit = outputUnit.displayName,
                onUnitChange = { newUnit ->
                    outputUnit = ChargeUnit.entries.first { it.displayName == newUnit }
                })
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = formulaMessage, style = MaterialTheme.typography.bodyLarge)
    }
}


@Composable
private fun UnitSelector(
    modifier: Modifier = Modifier, unit: String, onUnitChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val units = ChargeUnit.values().map { it.displayName }

    ElevatedCard(modifier = modifier, shape = RoundedCornerShape(4.dp)) {
        Row(
            Modifier
                .background(Color.Gray.copy(alpha = 0.2f))
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.height(300.dp)
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(onClick = {
                        onUnitChange(unit)
                        expanded = false
                    }, text = { Text(text = unit) })
                }
            }
            Text(
                text = unit, modifier = Modifier
                    .padding(8.dp)
                    .weight(1f), maxLines = 1
            )
            Column(
                modifier = Modifier
            ) {
                IncreaseDecreaseIconButton(true)
                IncreaseDecreaseIconButton(false)
            }
        }
    }
}

@Composable
private fun IncreaseDecreaseIconButton(
    isIncrease: Boolean,
) {
    Icon(
        imageVector = if (isIncrease) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
        contentDescription = if (isIncrease) "Increase" else "Decrease"
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewElectricityConverterScreen() {
//    SharedTransitionLayout {
//        ElectricityConverterScreen(Modifier.fillMaxSize(), this) {} }
}
