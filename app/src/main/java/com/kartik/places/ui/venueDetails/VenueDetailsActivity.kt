/*
 * Created by Kartik Kumar Gujarati on 10/20/19 1:10 AM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.kartik.places.R
import kotlinx.android.synthetic.main.activity_details.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout


class VenueDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(detail_toolbar)
        title = ""
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        disableDragOnCollapsingToolbarLayout()
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

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        // Add a marker in Sydney and move the camera
        val seattle = LatLng(47.6062,-122.3321)
        mMap.addMarker(MarkerOptions().position(seattle).title("Marker in Seattle"))
        mMap.setMinZoomPreference(10.0f)
        mMap.setMaxZoomPreference(18.0f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle))
    }
}