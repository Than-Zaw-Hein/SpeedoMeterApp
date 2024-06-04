package com.example.speedometerapp.screen.electricityConverter

enum class ChargeUnit(val displayName: String, val toCoulomb: Double) {
    Coulomb("coulomb [C]", 1.0),
    Megacoulomb("megacoulomb [MC]", 1_000_000.0),
    Kilocoulomb("kilocoulomb [kC]", 1_000.0),
    Millicoulomb("millicoulomb [mC]", 0.001),
    Microcoulomb("microcoulomb [µC]", 1.0E-6),
    Nanocoulomb("nanocoulomb [nC]", 1.0E-9),
    Picocoulomb("picocoulomb [pC]", 1.0E-12),
    Abcoulomb("abcoulomb [abC]", 10.0),
    EMUofCharge("EMU of charge", 10.0),
    Statcoulomb("statcoulomb [stC]", 3.335640951982E-10),
    ESUofCharge("ESU of charge", 3.335640951982E-10),
    Franklin("franklin [Fr]", 3.335640951982E-10),
    AmpereHour("ampere-hour [A*h]", 3600.0),
    AmpereMinute("ampere-minute [A*min]", 60.0),
    AmpereSecond("ampere-second [A*s]", 1.0),
    Faraday("faraday (based on carbon 12)", 96485.309000004),
    ElementaryCharge("Elementary charge [e]", 1.60217733E-19)
}

fun convertCharge(value: Double, fromUnit: ChargeUnit, toUnit: ChargeUnit): Double {
    // Convert value to coulombs first
    val valueInCoulombs = value * fromUnit.toCoulomb
    // Convert from coulombs to the target unit
    return valueInCoulombs / toUnit.toCoulomb
}