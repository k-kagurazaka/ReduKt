package com.kkagurazaka.redukt.sample.presentation.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

fun <T, R> LiveData<T>.map(mapper: (T?) -> R?): LiveData<R> =
        Transformations.map(this, mapper)
