package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery

interface BaseDao<T> {
    @Insert
    open fun insertObjects(appData: List<T>): List<Long>

    @RawQuery
    open fun getObjects(query: SimpleSQLiteQuery): List<T>

    @Update
    open fun updateObjects(appData: List<T>): Int

    @Delete
    open fun deleteObjects(appData: List<T>): Int
}