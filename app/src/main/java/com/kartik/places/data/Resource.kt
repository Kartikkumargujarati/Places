/*
 * Created by Kartik Kumar Gujarati on 10/19/19 2:01 PM
 * Copyright (c) 2019. All rights reserved.
 */

package com.kartik.places.data

// A generic class that contains data and status about loading this data.
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?, msg: String? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}