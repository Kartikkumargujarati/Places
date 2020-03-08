/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:34 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.data.remote

import com.kartik.places.model.VenueDetails
import com.kartik.places.model.VenueList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenueRemoteService {

    @GET("/v2/venues/search?v=20191019&ll=30.274636,-97.740399")
    suspend fun searchVenues(@Query("query") query: String, @Query("client_secret") clientSecret: String ,@Query("client_id") clientId: String): Response<VenueList>

    @GET("/v2/venues/{venueId}?v=20191019")
    suspend fun getVenueDetails(@Path("venueId") venueId: String, @Query("client_secret") clientSecret: String, @Query("client_id") clientId: String): Response<VenueDetails>
}