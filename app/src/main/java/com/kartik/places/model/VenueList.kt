/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:03 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Data class for Venue List
 */
data class VenueList(
    val response: Response
)

data class Response(
    val venues: List<Venue>
)

@Parcelize
@Entity(tableName = "venue_table")
data class Venue(
    val categories: List<Category>?,
    @PrimaryKey
    val id: String,
    val location: Location? = null,
    val name: String,
    var isFavorite: Boolean = false,
    var isFavoriteLoading: Boolean = false,
    var contact: Contact? = null,
    var hours: Hours? = null,
    var rating: Double? = null,
    var url: String? = ""
) : Parcelable {

    /*
    * Helper function to clone some of the properties.
    */
    fun clone(venue: Venue) {
        contact = venue.contact
        hours = venue.hours
        rating = venue.rating
        url = venue.url
    }
}

@Parcelize
data class Category(
    val icon: Icon,
    val name: String
) : Parcelable {
    fun getIconUrl(): String {
        return "${icon.prefix}88${icon.suffix}"
    }
}

@Parcelize
data class Icon(
    val prefix: String,
    val suffix: String
) : Parcelable

@Parcelize
data class Location(
    val distance: Int = -1,
    val address: String?,
    val city: String,
    val lat: Double,
    val lng: Double,
    val postalCode: String,
    val state: String
) : Parcelable {

    /*
    * Converting distance from meters to miles and returning as readable string.
    */
    fun getDistanceInMiles(): String {
        return String.format("%.2f mi", distance / 1609.344)
    }
}

@Parcelize
data class Contact(
    val formattedPhone: String?
) : Parcelable

@Parcelize
data class Hours(
    val status: String?
) : Parcelable