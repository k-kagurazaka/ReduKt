package com.kkagurazaka.redukt.sample.infra

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface TodoCommandDao {

    @Query("SELECT * FROM Todo WHERE id = :id")
    fun findById(id: Int): Todo

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo): Int
}
