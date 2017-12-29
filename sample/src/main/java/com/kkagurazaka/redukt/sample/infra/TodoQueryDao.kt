package com.kkagurazaka.redukt.sample.infra

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.kkagurazaka.redukt.sample.query.Todo

@Dao
interface TodoQueryDao {

    @Query("SELECT * FROM Todo")
    fun findAll(): List<Todo>

    @Query("SELECT * FROM Todo where completed = :completed")
    fun findBy(completed: Boolean): List<Todo>
}
