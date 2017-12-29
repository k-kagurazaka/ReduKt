package com.kkagurazaka.redukt.sample.infra

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index(value = ["completed"])])
class Todo(
        var text: String,
        var completed: Boolean = false
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
