/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:55 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.data

import androidx.lifecycle.MutableLiveData
import com.kartik.places.BuildConfig
import com.kartik.places.data.local.VenueDao
import com.kartik.places.data.remote.VenueRemoteServiceImpl
import com.kartik.places.model.Venue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/*
* The VenueRepository class acts as the data layer for the entire app. This class knows the source of data and how to fetch it.
* */
class VenueRepository(private val venueDao: VenueDao, private val venueRemoteService: VenueRemoteServiceImpl) {

    // Get Venues from remote server given a search keyword.
    fun getVenuesFromSearch(searchKey: String, result: MutableLiveData<Resource<List<Venue>>>) {
        result.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = venueRemoteService.getRemoteService().searchVenues(searchKey, BuildConfig.CLIENT_SECRET, BuildConfig.CLIENT_ID)
                if (response.isSuccessful && response.code() == 200) {
                    val venues = response.body()!!.response.venues
                    returnListData(venues, result, Status.SUCCESS)
                } else {
                    // handle error
                    returnListData(result = result, status = Status.ERROR)
                }
            } catch (exception: Exception) {
                // handle error
                returnListData(result = result, status = Status.ERROR)
            }
        }
    }

    // Get VenueDetails from a given venue
    fun getVenueDetails(venue: Venue, result: MutableLiveData<Resource<Venue>>) {
        result.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = venueRemoteService.getRemoteService().getVenueDetails(venue.id, BuildConfig.CLIENT_SECRET, BuildConfig.CLIENT_ID)
                if (response.isSuccessful && response.code() == 200) {
                    venue.clone(response.body()!!.response.venue)
                    returnDetailsData(venue, result, Status.SUCCESS)
                } else {
                    // handle error
                    returnDetailsData(result = result, status = Status.ERROR)
                }
            } catch (exception: Exception) {
                // handle error
                returnDetailsData(result = result, status = Status.ERROR)
            }
        }

    }

    // Favorite or un-favorite a venue and update the object appropriately. Used from Search List
    fun favoriteUnfavoriteAVenue(venue: Venue, result: MutableLiveData<Resource<Venue>>) {
        result.value = Resource.loading(venue)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                venue.isFavoriteLoading = false
                //if already favorited, un-favorite. If not already favorited, favorite it.
                if (!venue.isFavorite) {
                    venueDao.addVenueToFavorites(venue)
                    venue.isFavorite = true
                } else {
                    venueDao.removeVenueFromFavorites(venue)
                    venue.isFavorite = false
                }
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(venue)
                }

            } catch (exception: Exception) {
                withContext(Dispatchers.Main) { result.value = Resource.error("Could not favorite a Venue", venue) }
            }
        }
    }

    // Helper function
    private suspend fun returnListData(venueList: List<Venue>? = null, result: MutableLiveData<Resource<List<Venue>>>, status: Status) {
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
        when(status) {
            Status.SUCCESS -> {
                // sorting venues in ascending order of distance.
                withContext(Dispatchers.Main) { result.value = Resource.success(venueList?.sortedBy { it.location?.distance }) }
            }
            Status.ERROR -> {
                withContext(Dispatchers.Main) { result.value = Resource.error("Unable to load data") }
            }
        }
    }

    private suspend fun returnDetailsData(venue: Venue? = null, result: MutableLiveData<Resource<Venue>>, status: Status) {
        val favVenueList = venueDao.getAllFavoriteVenues()
        if (venue != null) {
            for (favVenue in favVenueList) {
                if (venue.id == favVenue.id) {
                    venue.isFavorite = true
                }
            }

        }
        when(status) {
            Status.SUCCESS -> {
                withContext(Dispatchers.Main) { result.value = Resource.success(venue) }
            }
            Status.ERROR -> {
                withContext(Dispatchers.Main) { result.value = Resource.error("Unable to load data") }
            }
        }
    }
}