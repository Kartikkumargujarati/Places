/*
 * Created by Kartik Kumar Gujarati on 10/20/19 3:20 AM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.model

/**
 * Data class for Venue Details
 */
data class VenueDetails(
    val response: DetailedResponse
 )

data class DetailedResponse(
    val venue: Venue
)


