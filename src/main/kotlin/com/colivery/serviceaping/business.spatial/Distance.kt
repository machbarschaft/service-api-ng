package com.colivery.serviceaping.business.spatial

private const val earthRadiusKm: Double = 6372.8

class Distance {
    companion object {
        @JvmStatic
        fun haversine(position: Coordinate, position2: Coordinate): Double {
            val dLat = Math.toRadians(position2.latitude - position.latitude)
            val dLon = Math.toRadians(position2.longitude - position.longitude)
            val originLat = Math.toRadians(position.latitude)
            val destinationLat = Math.toRadians(position2.latitude)

            val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat)
            val c = 2 * Math.asin(Math.sqrt(a))
            return earthRadiusKm * c
        }

        @JvmStatic
        fun coordinatesWhenTravelingInDirectionForDistance(coordinate: Coordinate, distance: Float, bearing: Float): Coordinate {
            val latitudeInRadians = Math.toRadians(coordinate.latitude)
            val longitudeInRadians = Math.toRadians(coordinate.longitude)
            val latitudeInRadians2 = Math.asin(Math.sin(latitudeInRadians) * Math.cos(distance / earthRadiusKm) +
                    Math.cos(latitudeInRadians) * Math.sin(distance / earthRadiusKm) * Math.cos(bearing.toDouble()))
            val longitudeInRadians2 = longitudeInRadians + Math.atan2(
                    Math.sin(bearing.toDouble()) * Math.sin(distance / earthRadiusKm) * Math.cos(latitudeInRadians),
                    Math.cos(distance / earthRadiusKm) - Math.sin(latitudeInRadians) * Math.sin(latitudeInRadians))
            return Coordinate(Math.toDegrees(latitudeInRadians2), Math.toDegrees(longitudeInRadians2))
        }
    }
}
