/*
 * Created by Kartik Kumar Gujarati on 10/19/19 3:34 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.ui.venueList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kartik.places.R
import com.kartik.places.model.Venue
import kotlinx.android.synthetic.main.list_item_content.view.*

class VenueListAdapter(private var venues: List<Venue>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_content, parent, false)
        return VenueViewHolder(view)
    }

    override fun getItemCount(): Int {
        return venues.size
    }

    fun setVenues(venues: List<Venue>) {
        this.venues = venues
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VenueViewHolder).bindData(venues[position])
    }

    internal inner class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(venue: Venue) {
            itemView.venue_name_tv.text = venue.name
            venue.categories?.firstOrNull().let {
                Glide.with(itemView.context).load(it?.getIconUrl()).into(itemView.venue_cat_iv)
                itemView.venue_cat_name_tv.text = it?.name
            }
            itemView.venue_distance_tv.text = venue.location?.getDistanceInMiles()
            if (venue.isFavorite) {
                itemView.venue_fav_iv.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_favorite_fill, null))
            } else {
                itemView.venue_fav_iv.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_favorite_empty, null))
            }

            itemView.setOnClickListener { onClickListener.onVenueClick(venue) }
            itemView.venue_fav_iv.setOnClickListener { onClickListener.onVenueFav(venue) }
            if (venue.isFavoriteLoading) {
                itemView.venue_fav_pb.visibility = View.VISIBLE
            } else {
                itemView.venue_fav_pb.visibility = View.GONE
            }
        }
    }

    interface OnClickListener {
        fun onVenueClick(venue: Venue)
        fun onVenueFav(venue: Venue)
    }
}