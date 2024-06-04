package com.example.speedometerapp.screen.volumnConverter

enum class VolumeUnit(val symbol: String, val conversionFactor: Double) {
    LITER("liter  [L,l]", 1.0),
    BARREL_DRY_US("bbl dry (US)", 115.6271236039),
    PINT_DRY_US("pt dry (US)", 0.5506104714),
    QUART_DRY_US("qt dry (US)", 1.1012209428),
    PECK_US("pk (US)", 8.8097675424),
    PECK_UK("pk (UK)", 9.09218),
    BUSHEL_US("bu (US)", 35.2390701696),
    BUSHEL_UK("bu (UK)", 36.36872),
    COR_BIBLICAL("cor (Biblical)", 219.9999892918),
    HOMER_BIBLICAL("homer (Biblical)", 219.9999892918),
    EPHAH_BIBLICAL("ephah (Biblical)", 21.9999989292),
    SEAH_BIBLICAL("seah (Biblical)", 7.3333329764),
    OMER_BIBLICAL("omer (Biblical)", 2.1999998929),
    CAB_BIBLICAL("cab (Biblical)", 1.2222221627),
    LOG_BIBLICAL("log (Biblical)", 0.3055555407);

    companion object {
        fun fromSymbol(symbol: String): VolumeUnit {
            return entries.first() { it.symbol == symbol }
        }


    }
}