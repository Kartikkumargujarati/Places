/*
 * Created by Kartik Kumar Gujarati on 10/19/19 1:47 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueList

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.kartik.places.R
import kotlinx.android.synthetic.main.activity_list.*

class VenueListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> return true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
