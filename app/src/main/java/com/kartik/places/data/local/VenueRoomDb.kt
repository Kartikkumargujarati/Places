/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:27 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kartik.places.model.Venue
import com.kartik.places.util.RoomTypeConverter

@Database(entities = [Venue::class], version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class VenueRoomDb : RoomDatabase() {

    abstract fun venueDao(): VenueDao

    companion object {
        @Volatile
        private var INSTANCE: VenueRoomDb? = null

        fun getDatabase(context: Context): VenueRoomDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VenueRoomDb::class.java,
                    "Venue_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}