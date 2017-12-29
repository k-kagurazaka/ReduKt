package com.kkagurazaka.redukt.sample.infra

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoCommandDao(): TodoCommandDao

    abstract fun todoQueryDao(): TodoQueryDao
}
