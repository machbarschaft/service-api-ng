package com.colivery.serviceaping.business.spatial

import com.colivery.serviceaping.business.spatial.Distance.Companion.coordinatesWhenTravelingInDirectionForDistance

data class Bounds(val sw: Coordinate, val ne: Coordinate)
data class Neighbors(val nw: String, val n: String, val ne: String,
                     val w: String, val e: String,
                     val sw: String, val s: String, val se: String)

private const val base32 = "0123456789bcdefghjkmnpqrstuvwxyz"

class GeoHash {
    companion object {
        @JvmStatic
        fun encode(lat: Double, lon: Double, precision: Int = 5): String {
            var idx = 0 // index into base32 map
            var bit = 0 // each char holds 5 bitseo
            var evenBit = true
            var geohash = ""

            var latMin = -90.0
            var latMax = 90.0
            var lonMin = -180.0
            var lonMax = 180.0

            while (geohash.length < precision) {
                if (evenBit) {
                    // bisect E-W longitude
                    val lonMid = (lonMin + lonMax) / 2.0
                    if (lon >= lonMid) {
                        idx = idx * 2 + 1
                        lonMin = lonMid
                    } else {
                        idx = idx * 2
                        lonMax = lonMid
                    }
                } else {
                    // bisect N-S latitude
                    val latMid = (latMin + latMax) / 2.0
                    if (lat >= latMid) {
                        idx = idx * 2 + 1
                        latMin = latMid
                    } else {
                        idx *= 2
                        latMax = latMid
                    }
                }
                evenBit = !evenBit

                if (++bit == 5) {
                    // 5 bits gives us a character: append it and start over
                    geohash += base32[idx]
                    bit = 0
                    idx = 0
                }
            }

            return geohash
        }

        @JvmStatic
        fun decode(geohash: String): Coordinate {

            val bounds = bounds(geohash); // <-- the hard work

            val latMin = bounds.sw.latitude
            val lonMin = bounds.sw.longitude
            val latMax = bounds.ne.latitude
            val lonMax = bounds.ne.longitude;

            val lat = (latMin + latMax) / 2
            val lon = (lonMin + lonMax) / 2

            return Coordinate(latitude = lat, longitude = lon)
        }

        @JvmStatic
        fun bounds(p_geohash: String): Bounds {
            var geohash = p_geohash
            if (geohash.isEmpty()) throw Exception("Invalid geohash")

            geohash = geohash.toLowerCase()

            var evenBit = true
            var latMin = -90.0
            var latMax = 90.0
            var lonMin = -180.0
            var lonMax = 180.0

            for (element in geohash) {
                val idx = base32.indexOf(element)
                if (idx == -1) throw Exception("Invalid geohash")

                for (n in 4 downTo 0) {
                    val bitN = idx shr n and 1
                    if (evenBit) {
                        // longitude
                        val lonMid = (lonMin + lonMax) / 2.0
                        if (bitN == 1) {
                            lonMin = lonMid
                        } else {
                            lonMax = lonMid
                        }
                    } else {
                        // latitude
                        val latMid = (latMin + latMax) / 2.0
                        if (bitN == 1) {
                            latMin = latMid
                        } else {
                            latMax = latMid
                        }
                    }
                    evenBit = !evenBit
                }
            }

            return Bounds(Coordinate(latMin, lonMin), Coordinate(latMax, lonMax))
        }

        private fun adjacent(p_geohash: String, p_direction: String): String {
            val geohash = p_geohash.toLowerCase()
            val direction = p_direction.toLowerCase()

            if (geohash.isEmpty()) throw Exception("Invalid geohash")
            if ("nsew".indexOf(direction) == -1) throw Exception("Invalid direction")

            val neighbour = mapOf(
                    "n" to arrayOf("p0r21436x8zb9dcf5h7kjnmqesgutwvy", "bc01fg45238967deuvhjyznpkmstqrwx"),
                    "s" to arrayOf("14365h7k9dcfesgujnmqp0r2twvyx8zb", "238967debc01fg45kmstqrwxuvhjyznp"),
                    "e" to arrayOf("bc01fg45238967deuvhjyznpkmstqrwx", "p0r21436x8zb9dcf5h7kjnmqesgutwvy"),
                    "w" to arrayOf("238967debc01fg45kmstqrwxuvhjyznp", "14365h7k9dcfesgujnmqp0r2twvyx8zb")
            )
            val border = mapOf(
                    "n" to arrayOf("prxz", "bcfguvyz"),
                    "s" to arrayOf("028b", "0145hjnp"),
                    "e" to arrayOf("bcfguvyz", "prxz"),
                    "w" to arrayOf("0145hjnp", "028b")
            )

            val lastCh = geohash.takeLast(1)    // last character of hash
            var parent = geohash.substringBeforeLast(lastCh) // hash without last character

            val type = geohash.length % 2

            // check for edge-cases which don"t share common prefix
            if ((border[direction] ?: error(""))[type].indexOf(lastCh) != -1 && parent != "") {
                parent = adjacent(parent, direction)
            }

            // append letter for direction to parent
            return parent + base32.toCharArray()[(neighbour[direction]
                    ?: error(""))[type].indexOf(lastCh)]
        }

        @JvmStatic
        fun neighbours(geohash: String): Neighbors {
            return Neighbors(
                    adjacent(adjacent(geohash, "n"), "w"),
                    adjacent(geohash, "n"),
                    adjacent(adjacent(geohash, "n"), "e"),
                    adjacent(geohash, "w"),
                    adjacent(geohash, "e"),
                    adjacent(adjacent(geohash, "s"), "w"),
                    adjacent(geohash, "s"),
                    adjacent(adjacent(geohash, "s"), "e")
            )
        }

        @JvmStatic
        fun buildMinMaxGeoHashesOfCircle(startLocation: Coordinate, radius: Float): Pair<String, String> {
            val north = coordinatesWhenTravelingInDirectionForDistance(startLocation, radius, 0f)
            val east = coordinatesWhenTravelingInDirectionForDistance(startLocation, radius, 90f)
            val south = coordinatesWhenTravelingInDirectionForDistance(startLocation, radius, 180f)
            val west = coordinatesWhenTravelingInDirectionForDistance(startLocation, radius, 270f)

            val min = encode(south.latitude, west.longitude)
            val max = encode(north.latitude, east.longitude)

            return Pair(min, max)
        }
    }
}
