/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:55 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.data

import com.kartik.places.BuildConfig
import com.kartik.places.data.local.VenueDao
import com.kartik.places.data.remote.VenueRemoteServiceImpl
import com.kartik.places.model.Venue
import java.lang.Exception

/*
* The VenueRepository class acts as the data layer for the entire app. This class knows the source of data and how to fetch it.
* */
class VenueRepository(private val venueDao: VenueDao, private val venueRemoteService: VenueRemoteServiceImpl) {

    // Get Venues from remote server given a search keyword.
    suspend fun getVenuesFromSearch(searchKey: String): Resource<List<Venue>> {
        return try {
            val response = venueRemoteService.getRemoteService().searchVenues(searchKey, BuildConfig.CLIENT_SECRET, BuildConfig.CLIENT_ID)
            if (response.isSuccessful && response.code() == 200) {
                val venues = response.body()!!.response.venues
                returnListData(venues)
            } else {
                // handle error
                Resource.error("Unable to load data")
            }
        } catch (exception: Exception) {
            // handle error
            Resource.error("Unable to load data")
        }
    }

    // Get VenueDetails from a given venue
    suspend fun getVenueDetails(venue: Venue): Resource<Venue> {
        return try {
            val response = venueRemoteService.getRemoteService().getVenueDetails(venue.id, BuildConfig.CLIENT_SECRET, BuildConfig.CLIENT_ID)
            if (response.isSuccessful && response.code() == 200) {
                venue.clone(response.body()!!.response.venue)
                returnDetailsData(venue)
            } else {
                // handle error
                Resource.error("Unable to load data")
            }
        } catch (exception: Exception) {
            // handle error
            Resource.error("Unable to load data")
        }
    }

    // Favorite or un-favorite a venue and update the object appropriately. Used from Search List
    fun favoriteUnfavoriteAVenue(venue: Venue): Resource<Venue> {
        return try {
            venue.isFavoriteLoading = false
            //if already favorited, un-favorite. If not already favorited, favorite it.
            if (!venue.isFavorite) {
                venueDao.addVenueToFavorites(venue)
                venue.isFavorite = true
            } else {
                venueDao.removeVenueFromFavorites(venue)
                venue.isFavorite = false
            }
            Resource.success(venue)
        } catch (exception: Exception) {
            Resource.error("Could not favorite a Venue", venue)
        }
    }

    // Helper function
    private fun returnListData(venueList: List<Venue>? = null): Resource<List<Venue>> {
        val favVenueList = venueDao.getAllFavoriteVenues()
        if (venueList != null) {
            for (venue in venueList) {
                for (favVenue in favVenueList) {
                    if (venue.id == favVenue.id) {
                        venue.isFavorite = true
                    }
                }
            }
        }
        return Resource.success(venueList?.sortedBy { it.location?.distance })
    }

    private fun returnDetailsData(venue: Venue? = null): Resource<Venue> {
        val favVenueList = venueDao.getAllFavoriteVenues()
        if (venue != null) {
            for (favVenue in favVenueList) {
                if (venue.id == favVenue.id) {
                    venue.isFavorite = true
                }
            }
        }
        return Resource.success(venue)
    }
}