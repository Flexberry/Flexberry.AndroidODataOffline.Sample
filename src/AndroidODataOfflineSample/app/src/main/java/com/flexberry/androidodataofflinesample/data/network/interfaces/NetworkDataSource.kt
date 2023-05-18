package com.flexberry.androidodataofflinesample.data.network.interfaces

import com.flexberry.androidodataofflinesample.data.query.QuerySettings

interface NetworkDataSource<T> {
    fun createObjects(vararg dataObjects: T): Int
    fun createObjects(listObjects: List<T>): Int
    fun readObjects(querySettings: QuerySettings? = null): List<T>
    fun updateObjects(vararg dataObjects: T): Int
    fun updateObjects(listObjects: List<T>): Int
    fun deleteObjects(vararg dataObjects: T): Int
    fun deleteObjects(listObjects: List<T>): Int
}