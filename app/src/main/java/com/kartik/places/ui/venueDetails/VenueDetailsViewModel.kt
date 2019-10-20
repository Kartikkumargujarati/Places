/*
 * Created by Kartik Kumar Gujarati on 10/20/19 3:11 AM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartik.places.data.Resource
import com.kartik.places.data.VenueRepository
import com.kartik.places.model.Venue

/**
 * A ViewModel for [VenueDetailsActivity]
 */
class VenueDetailsViewModel(private val repository: VenueRepository): ViewModel() {

    private val _venue = MutableLiveData<Resource<Venue>>()
    val venue : LiveData<Resource<Venue>>
        get() = _venue

    private val _favVenue = MutableLiveData<Resource<Venue>>()
    val favVenue : LiveData<Resource<Venue>>
        get() = _favVenue

    fun getVenueDetails(venue: Venue) {
        repository.getVenueDetails(venue, _venue)
    }

    fun favoriteAVenue(venue: Venue) {
        repository.favoriteUnfavoriteAVenue(venue, _favVenue)
    }
}

class VenueDetailsViewModelFactory(private val repository: VenueRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VenueDetailsViewModel(repository) as T
    }
}