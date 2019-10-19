/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:03 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class VenueList(
    val response: Response
)

data class Response(
    val venues: List<Venue>
)

@Entity(tableName = "venue_table")
data class Venue(
    val categories: List<Category>,
    @PrimaryKey
    val id: String,
    val location: Location,
    val name: String,
    var isFavorite: Boolean = false
)

data class Category(
    val icon: Icon,
    val name: String
)

data class Icon(
    val prefix: String,
    val suffix: String
)

data class Location(
    val distance: Int
)
