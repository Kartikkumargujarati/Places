/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:39 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VenueRemoteServiceImpl {

    fun getRemoteService(): VenueRemoteService {
        return Retrofit.Builder()
            .baseUrl(FOURSQUARE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(VenueRemoteService::class.java)
    }

    companion object {
        const val FOURSQUARE_URL = "https://api.foursquare.com/v2/venues"
    }
}