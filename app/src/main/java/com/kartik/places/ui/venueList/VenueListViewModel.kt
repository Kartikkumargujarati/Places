/*
 * Created by Kartik Kumar Gujarati on 10/19/19 3:33 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueList

import androidx.lifecycle.*
import com.kartik.places.data.Resource
import com.kartik.places.data.VenueRepository
import com.kartik.places.model.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A ViewModel for [VenueListActivity]
 */
class VenueListViewModel(private val repository: VenueRepository): ViewModel() {

    private val _venueList = MutableLiveData<Resource<List<Venue>>>()
    val venueList : LiveData<Resource<List<Venue>>>
        get() = _venueList

    private val _favVenue = MutableLiveData<Resource<Venue>>()
    val favVenue : LiveData<Resource<Venue>>
        get() = _favVenue

    fun getVenues(searchKey: String) {
        _venueList.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _venueList.postValue(repository.getVenuesFromSearch(searchKey))
        }
    }

    fun favoriteAVenue(venue: Venue) {
        _favVenue.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _favVenue.postValue(repository.favoriteUnfavoriteAVenue(venue))
        }
    }
}

class VenueListViewModelFactory(private val repository: VenueRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VenueListViewModel(repository) as T
    }
}