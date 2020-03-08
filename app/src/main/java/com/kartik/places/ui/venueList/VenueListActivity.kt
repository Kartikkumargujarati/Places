/*
 * Created by Kartik Kumar Gujarati on 10/19/19 1:47 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kartik.places.R
import com.kartik.places.data.Resource
import com.kartik.places.data.Status
import com.kartik.places.data.VenueRepository
import com.kartik.places.data.local.VenueRoomDb
import com.kartik.places.data.remote.VenueRemoteServiceImpl
import com.kartik.places.model.Venue
import com.kartik.places.ui.venueDetails.VenueDetailsActivity
import com.kartik.places.ui.venueDetails.VenueDetailsActivity.Companion.ARG_VENUE
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.list_content.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * The [VenueListActivity] represents a list of venues that are retrieved from the FourSquaye API.
 */
class VenueListActivity : AppCompatActivity() {

    private lateinit var viewModel: VenueListViewModel
    private lateinit var adapter: VenueListAdapter
    private val DETAILS_REQUEST_CODE = 101
    private var venue: Venue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
        val venueDao = VenueRoomDb.getDatabase(applicationContext).venueDao()
        val repository = VenueRepository(venueDao, VenueRemoteServiceImpl())
        viewModel = ViewModelProviders.of(this, VenueListViewModelFactory(repository))[VenueListViewModel::class.java]
        viewModel.venueList.observe(::getLifecycle, ::updateList)
        viewModel.favVenue.observe(::getLifecycle, ::updateFavoriteVenue)
        setupRecyclerView(item_list)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        setupSearchView(searchView)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> { return true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            DETAILS_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val updatedVenue = data?.getParcelableExtra<Venue>(ARG_VENUE)
                    venue?.isFavorite = updatedVenue?.isFavorite!!
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    /********************       Helper methods        *****************/

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val layoutManager  = GridLayoutManager(this, resources.getInteger(R.integer.list_coloum_count))
        recyclerView.layoutManager = layoutManager
        adapter = VenueListAdapter(ArrayList(), object : VenueListAdapter.OnClickListener {
            override fun onVenueClick(venue: Venue) {
                // navigate to details
                startDetailsActivity(venue)
            }

            override fun onVenueFav(venue: Venue) {
                // fav a venue
                viewModel.favoriteAVenue(venue)
            }

        })
        recyclerView.adapter = adapter
    }

    private fun setupSearchView(searchView: SearchView) {
        var searchQuery = ""
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.toString().isEmpty()) {
                    submitQuery("")
                    searchQuery = ""
                } else {
                    submitQuery(query.toString())
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    val searchText = newText.toString().trim()
                    if (searchText == searchQuery)
                        return false
                    searchQuery = searchText
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300)  //debounce timeOut
                        if (searchText != searchQuery)
                            return@launch
                        submitQuery(searchText)
                    }
                } else {
                    submitQuery("")
                    searchQuery = ""
                }
                return true
            }
        })
    }

    private fun submitQuery(query: String) {
        if (query.isNullOrEmpty()) {
            adapter.setVenues(ArrayList())
            empty_search_tv.visibility = View.VISIBLE
            empty_search_tv.text = resources.getText(R.string.search_venue)
        } else {
            viewModel.getVenues(query)
        }
    }

    private fun updateList(resource: Resource<List<Venue>>?) {
        progress.visibility = View.GONE
        when(resource?.status) {
            Status.SUCCESS -> {
                if (resource.data.isNullOrEmpty()) {
                    empty_search_tv.visibility = View.VISIBLE
                    empty_search_tv.text = resources.getText(R.string.no_search_venues_found)
                } else {
                    empty_search_tv.visibility = View.GONE
                    adapter.setVenues(resource.data)
                }
            }
            Status.ERROR -> {
                adapter.setVenues(ArrayList())
                Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> progress.visibility = View.VISIBLE
        }
    }

    private fun updateFavoriteVenue(resource: Resource<Venue>?) {
        when(resource?.status) {
            Status.SUCCESS -> {
                resource.data?.isFavoriteLoading = false
            }
            Status.ERROR -> {
                resource.data?.isFavoriteLoading = false
                Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> resource.data?.isFavoriteLoading = true
        }
        adapter.notifyDataSetChanged()
    }

    private fun startDetailsActivity(venue: Venue) {
        this.venue = venue
        val intent = Intent(this, VenueDetailsActivity::class.java).apply {
            putExtra(ARG_VENUE, venue)
        }
        startActivityForResult(intent, DETAILS_REQUEST_CODE)
    }
}
