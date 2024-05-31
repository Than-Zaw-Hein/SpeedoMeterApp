package com.example.speedometerapp.screen.LengthCalculator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.speedometerapp.R
import com.example.speedometerapp.screen.LengthCalculator.LengthUnit.*

@Composable
fun LengthCalculatorScreen(modifier: Modifier, onBackPress: () -> Unit) {


    BackHandler { onBackPress() }

    var inputValue by remember { mutableStateOf("1") }
    var inputUnit by remember { mutableStateOf(Meters) }

    var outputUnit by remember { mutableStateOf(Feet) }
    var expendOutput by remember { mutableStateOf(false) }
    val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
    val outputValueDouble = convertLength(inputValueDouble, inputUnit, outputUnit)

    val formulaMessage = if (inputValue.isNotEmpty()) {
        try {
            val formula = getFormula(1.0, inputUnit, outputUnit)
            "1 ${inputUnit.name.lowercase()} = $formula ${outputUnit.name.lowercase()}"
        } catch (e: NumberFormatException) {
            "Invalid input value"
        }
    } else {
        "Please enter a value to convert"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.length_converter),
            contentDescription = "Length Converter Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(180.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) { // Input field
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = inputValue,
                singleLine = true,
                maxLines = 1,

                onValueChange = { inputValue = it },
                label = { Text(text = "Input Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Output field
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = outputValueDouble.toString(),
                onValueChange = {},
                enabled = true,
                label = { Text(text = "Output Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            LengthUnitSelector(
                modifier = Modifier.weight(1f),
                lengthUnit = inputUnit.name,
                onUnitChange = { inputUnit = it }
            )
            Text("=", style = MaterialTheme.typography.headlineLarge)
            LengthUnitSelector(
                modifier = Modifier.weight(1f),
                lengthUnit = outputUnit.name,
                onUnitChange = { outputUnit = it }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = formulaMessage, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun LengthUnitSelector(
    modifier: Modifier = Modifier,
    lengthUnit: String,
    onUnitChange: (LengthUnit) -> Unit
) {

    var expendInput by remember { mutableStateOf(false) }
    ElevatedCard(modifier = modifier, shape = RoundedCornerShape(4.dp)) {
        Row(
            Modifier
                .background(Color.Gray.copy(alpha = 0.2f))
                .clickable {
                    expendInput = true
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownMenu(
                modifier = Modifier
                    .height(300.dp)
                    .wrapContentHeight(),
                expanded = expendInput,
                onDismissRequest = { expendInput = false },
            ) {
                LengthUnit.entries.forEach { unit ->
                    DropdownMenuItem(onClick = {
                        onUnitChange(unit)
                        expendInput = false
                    }, text = { Text(text = unit.name) })
                }
            }
            Text(
                " $lengthUnit", modifier = Modifier.padding(8.dp)
            )
            Spacer(Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                IncreaseDecreaseIconButton(true)
                IncreaseDecreaseIconButton(false)
            }
        }
    }
}


@Composable
fun IncreaseDecreaseIconButton(
    isIncrease: Boolean,
) {
    Icon(
        imageVector = if (isIncrease) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
        contentDescription = if (isIncrease) "Increase" else "Decrease"
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLengthCalculatorScreen() {
    LengthCalculatorScreen(Modifier.fillMaxSize()) {}
}

private fun convertLength(value: Double, fromUnit: LengthUnit, toUnit: LengthUnit): Double {
    return when (fromUnit) {
        Meters -> when (toUnit) {
            Meters -> value
            Feet -> value * 3.28084
            Inches -> value * 39.3701
            Yards -> value * 1.09361
            Miles -> value * 0.000621371
            Centimeters -> value * 100
            Kilometers -> value / 1000
            Millimeters -> value * 1000
        }

        Feet -> when (toUnit) {
            Meters -> value / 3.28084
            Feet -> value
            Inches -> value * 12
            Yards -> value / 3
            Miles -> value / 5280
            Centimeters -> value * 30.48
            Kilometers -> value / 3280.84
            Millimeters -> value * 304.8
        }

        Inches -> when (toUnit) {
            Meters -> value / 39.3701
            Feet -> value / 12
            Inches -> value
            Yards -> value / 36
            Miles -> value / 63360
            Centimeters -> value * 2.54
            Kilometers -> value / 39370.1
            Millimeters -> value * 25.4
        }

        Yards -> when (toUnit) {
            Meters -> value / 1.09361
            Feet -> value * 3
            Inches -> value * 36
            Yards -> value
            Miles -> value / 1760
            Centimeters -> value * 91.44
            Kilometers -> value / 1093.61
            Millimeters -> value * 914.4
        }

        Miles -> when (toUnit) {
            Meters -> value / 0.000621371
            Feet -> value * 5280
            Inches -> value * 63360
            Yards -> value * 1760
            Miles -> value
            Centimeters -> value * 160934
            Kilometers -> value * 1.60934
            Millimeters -> value * 1.609e+6
        }

        Kilometers -> when (toUnit) {
            Meters -> value * 1000
            Feet -> value * 3280.84
            Inches -> value * 39370.1
            Yards -> value * 1093.61
            Miles -> value / 1.60934
            Centimeters -> value * 100000
            Kilometers -> value
            Millimeters -> value * 1e+6
        }

        Centimeters -> when (toUnit) {
            Meters -> value / 100
            Feet -> value / 30.48
            Inches -> value / 2.54
            Yards -> value / 91.44
            Miles -> value / 160934
            Centimeters -> value
            Kilometers -> value / 100000
            Millimeters -> value * 10
        }

        Millimeters -> when (toUnit) {
            Meters -> value / 1000
            Feet -> value / 304.8
            Inches -> value / 25.4
            Yards -> value / 914.4
            Miles -> value / 1.609e+6
            Centimeters -> value / 10
            Kilometers -> value / 1e+6
            Millimeters -> value
        }
    }
}

private fun getFormula(value: Double = 1.0, fromUnit: LengthUnit, toUnit: LengthUnit): Double {
    return when (fromUnit) {
        Meters -> when (toUnit) {
            Meters -> value
            Feet -> value * 3.28084
            Inches -> value * 39.3701
            Yards -> value * 1.09361
            Miles -> value * 0.000621371
            Centimeters -> value * 100
            Kilometers -> value / 1000
            Millimeters -> value * 1000
        }

        Feet -> when (toUnit) {
            Meters -> value / 3.28084
            Feet -> value
            Inches -> value * 12
            Yards -> value / 3
            Miles -> value / 5280
            Centimeters -> value * 30.48
            Kilometers -> value / 3280.84
            Millimeters -> value * 304.8
        }

        Inches -> when (toUnit) {
            Meters -> value / 39.3701
            Feet -> value / 12
            Inches -> value
            Yards -> value / 36
            Miles -> value / 63360
            Centimeters -> value * 2.54
            Kilometers -> value / 39370.1
            Millimeters -> value * 25.4
        }

        Yards -> when (toUnit) {
            Meters -> value / 1.09361
            Feet -> value * 3
            Inches -> value * 36
            Yards -> value
            Miles -> value / 1760
            Centimeters -> value * 91.44
            Kilometers -> value / 1093.61
            Millimeters -> value * 914.4
        }

        Miles -> when (toUnit) {
            Meters -> value / 0.000621371
            Feet -> value * 5280
            Inches -> value * 63360
            Yards -> value * 1760
            Miles -> value
            Centimeters -> value * 160934
            Kilometers -> value * 1.60934
            Millimeters -> value * 1.609e+6
        }

        Kilometers -> when (toUnit) {
            Meters -> value * 1000
            Feet -> value * 3280.84
            Inches -> value * 39370.1
            Yards -> value * 1093.61
            Miles -> value / 1.60934
            Centimeters -> value * 100000
            Kilometers -> value
            Millimeters -> value * 1e+6
        }

        Centimeters -> when (toUnit) {
            Meters -> value / 100
            Feet -> value / 30.48
            Inches -> value / 2.54
            Yards -> value / 91.44
            Miles -> value / 160934
            Centimeters -> value
            Kilometers -> value / 100000
            Millimeters -> value * 10
        }

        Millimeters -> when (toUnit) {
            Meters -> value / 1000
            Feet -> value / 304.8
            Inches -> value / 25.4
            Yards -> value / 914.4
            Miles -> value / 1.609e+6
            Centimeters -> value / 10
            Kilometers -> value / 1e+6
            Millimeters -> value
        }
    }
}

enum class LengthUnit {
    Meters, Feet, Inches, Yards, Miles, Kilometers, Centimeters, Millimeters
}