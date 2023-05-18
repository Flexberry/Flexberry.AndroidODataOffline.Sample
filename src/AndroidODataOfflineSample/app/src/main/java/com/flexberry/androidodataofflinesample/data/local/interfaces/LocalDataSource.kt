package com.flexberry.androidodataofflinesample.data.local.interfaces

import com.flexberry.androidodataofflinesample.data.query.QuerySettings

interface LocalDataSource<T> {
    fun createObjects(vararg dataObjects: T): Int
    fun createObjects(listObjects: List<T>): Int
    fun readObjects(querySettings: QuerySettings? = null): List<T>
    fun updateObjects(vararg dataObjects: T): Int
    fun updateObjects(listObjects: List<T>): Int
    fun deleteObjects(vararg dataObjects: T): Int
    fun deleteObjects(listObjects: List<T>): Int
}