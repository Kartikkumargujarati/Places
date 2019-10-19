/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:29 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.data.local

import androidx.room.*
import com.kartik.places.model.Venue

@Dao
interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addVenueToFavorites(venue: Venue)

    @Query("SELECT * from venue_table")
    fun getAllFavoriteVenues(): List<Venue>

    @Delete
    fun removeVenueFromFavorites(venue: Venue)
}