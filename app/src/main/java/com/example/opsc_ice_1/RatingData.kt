package com.example.opsc_ice_1

data class Rating(val advice: String, val rating: Float)

object RatingRepository {
    val ratings = mutableListOf<Rating>()
}
