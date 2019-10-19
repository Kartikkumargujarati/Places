/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:45 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.kartik.places.model.Category
import com.kartik.places.model.Location

class RoomTypeConverter {

    @TypeConverter
    fun stringToCategoryList(value: String): List<Category?>? {
        val list = Gson().fromJson(value, Array<Category?>::class.java) as Array<Category?>
        return list.toList()
    }

    @TypeConverter
    fun categoryListToString(list: List<Category?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToLocation(value: String): Location? {
        return Gson().fromJson(value, Location::class.java) as Location
    }

    @TypeConverter
    fun locationToString(location: Location): String? {
        return Gson().toJson(location)
    }
}