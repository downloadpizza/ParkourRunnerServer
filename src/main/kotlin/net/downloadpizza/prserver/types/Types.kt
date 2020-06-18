package net.downloadpizza.prserver.types

import kotlin.math.*

interface Coordinates {
    val latitude: Double
    val longitude: Double

    operator fun component1() = latitude
    operator fun component2() = longitude
}

data class SimpleCoordinates(
    override val latitude: Double,
    override val longitude: Double
): Coordinates

data class GeoCoordinates(
    override val latitude: Double,
    override val longitude: Double,
    val accuracy: Double,
    val altitude: Double,
    val heading: Double,
    val speed: Double
): Coordinates

data class GeoPosition(
    val coords: GeoCoordinates,
    val timestamp: Double,
    val mocked: Boolean? = null
)

private fun Double.toRadians() = PI * (this / 180)
private infix fun Double.delta(other: Double) = abs(this - other)

private fun Coordinates.radians(): Pair<Double, Double> = Pair(this.latitude.toRadians(), this.longitude.toRadians())

fun distanceBetween(cord1: Coordinates, cord2: Coordinates): Double {
    val lon1 = cord1.longitude.toRadians()
    val lat1 = cord1.latitude.toRadians()

    val lon2 = cord2.longitude.toRadians()
    val lat2 = cord2.latitude.toRadians()


    val r = 6371e3; // metres
    val deltaLat = lat2 delta lat1
    val deltaLon = lon2 delta lon1

    val a = sin(deltaLat / 2).pow(2) +
            cos(lat1) * cos(lat2) * sin(deltaLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c;
}

fun bearingBetween(cord1: Coordinates, cord2: Coordinates): Double {
    val lon1 = cord1.longitude.toRadians()
    val lat1 = cord1.latitude.toRadians()

    val lon2 = cord2.longitude.toRadians()
    val lat2 = cord2.latitude.toRadians()

    // L = long | θ = lat

    val deltaLong = lon1 delta lon2

    // X = cos θb * sin ∆L
    val x = cos(lat2) * sin(deltaLong)

    // Y = cos θa * sin θb – sin θa * cos θb * cos ∆L
    val y = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLong)

    // β = atan2(X,Y)
    return atan2(x, y)
}