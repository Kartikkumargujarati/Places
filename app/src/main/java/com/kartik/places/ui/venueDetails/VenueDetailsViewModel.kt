/*
 * Created by Kartik Kumar Gujarati on 10/20/19 3:11 AM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueDetails

import androidx.lifecycle.*
import com.kartik.places.data.Resource
import com.kartik.places.data.VenueRepository
import com.kartik.places.model.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        _venue.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _venue.postValue(repository.getVenueDetails(venue))
        }
    }

    fun favoriteAVenue(venue: Venue) {
        _favVenue.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _favVenue.postValue(repository.favoriteUnfavoriteAVenue(venue))
        }
    }
}

class VenueDetailsViewModelFactory(private val repository: VenueRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VenueDetailsViewModel(repository) as T
    }
}