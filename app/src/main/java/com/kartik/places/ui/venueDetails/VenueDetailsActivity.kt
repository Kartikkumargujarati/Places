/*
 * Created by Kartik Kumar Gujarati on 10/20/19 1:10 AM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.kartik.places.R
import kotlinx.android.synthetic.main.activity_details.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout
import com.kartik.places.data.Resource
import com.kartik.places.data.Status
import com.kartik.places.data.VenueRepository
import com.kartik.places.data.local.VenueRoomDb
import com.kartik.places.data.remote.VenueRemoteServiceImpl
import com.kartik.places.model.Venue
import kotlinx.android.synthetic.main.layout_favorite_iv.*


class VenueDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var venue: Venue? = null
    private lateinit var viewModel: VenueDetailsViewModel
    private var isUpdated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(detail_toolbar)
        title = ""
        venue = intent.getParcelableExtra(ARG_VENUE)
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        disableDragOnCollapsingToolbarLayout()

        val venueDao = VenueRoomDb.getDatabase(applicationContext).venueDao()
        val repository = VenueRepository(venueDao, VenueRemoteServiceImpl())
        viewModel = ViewModelProviders.of(this, VenueDetailsViewModelFactory(repository))[VenueDetailsViewModel::class.java]
        viewModel.venue.observe(::getLifecycle, ::updateDetails)
        viewModel.favVenue.observe(::getLifecycle, ::updateFavoriteVenue)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getVenueDetails(venue!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            handleBack()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        handleBack()
    }

    /********************       Helper methods        *****************/

    private fun updateDetails(resource: Resource<Venue>?) {
        progress.visibility = View.GONE
        when(resource?.status) {
            Status.SUCCESS -> {
                if (resource.data == null) {
                    error_tv.visibility = View.VISIBLE
                } else {
                    error_tv.visibility = View.GONE
                    updateUI(resource.data)
                }
            }
            Status.ERROR -> {
                error_tv.visibility = View.VISIBLE
                Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> progress.visibility = View.VISIBLE
        }
    }

    private fun updateFavoriteVenue(resource: Resource<Venue>?) {
        venue_fav_pb.visibility = View.GONE
        when(resource?.status) {
            Status.SUCCESS -> {
                isUpdated = true
                updateFavIcon(resource.data!!)
            }
            Status.ERROR -> {
                Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> venue_fav_pb.visibility = View.VISIBLE
        }
    }

    private fun updateUI(venue: Venue) {
        venue_name_tv.text = venue.name
        venue.categories?.firstOrNull().let { venue_cat_name_tv.text = it?.name }
        venue_rating_tv.text = venue.rating.toString()
        venue_distance_tv.text = venue.location?.getDistanceInMiles()
        venue_address_tv.text = "${venue.location?.address} ${venue.location?.city} ${venue.location?.state} ${venue.location?.postalCode}"
        venue_status_tv.text = venue.hours?.status
        venue_phone_tv.text = venue.contact?.formattedPhone
        venue_website_tv.text = venue.url
        updateFavIcon(venue)
        venue_fav_iv.setOnClickListener { viewModel.favoriteAVenue(venue) }
    }

    private fun updateFavIcon(venue: Venue) {
        if (venue.isFavorite) {
            venue_fav_iv.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_fill, null))
        } else {
            venue_fav_iv.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_empty, null))
        }
    }

    /*
    * Disabling the drag on the collapsing toolbar in-order to be able to drag within the mapView.
    */
    private fun disableDragOnCollapsingToolbarLayout() {
        val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar)
        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = AppBarLayout.Behavior()
        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
        params.behavior = behavior
    }

    private fun handleBack() {
        if (isUpdated) {
            val intent = Intent().apply {
                putExtra(ARG_VENUE, venue)
            }
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        // Add a marker in Sydney and move the camera
        val seattle = LatLng(47.6062,-122.3321)
        mMap.addMarker(MarkerOptions().position(seattle).title("Marker in Seattle"))
        mMap.setMinZoomPreference(10.0f)
        mMap.setMaxZoomPreference(18.0f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle))
    }

    companion object {
        const val ARG_VENUE = "venue"
    }
}