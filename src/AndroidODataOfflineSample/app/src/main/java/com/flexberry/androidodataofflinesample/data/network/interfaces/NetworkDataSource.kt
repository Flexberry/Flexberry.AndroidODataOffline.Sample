package com.flexberry.androidodataofflinesample.data.network.interfaces

import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import com.flexberry.androidodataofflinesample.data.query.View

interface NetworkDataSource<T> {
    open fun createObjects(vararg dataObjects: T): Int
    open fun createObjects(listObjects: List<T>): Int
    open fun readObjects(querySettings: QuerySettings? = null, view: View? = null): List<T>
    open fun updateObjects(vararg dataObjects: T): Int
    open fun updateObjects(listObjects: List<T>): Int
    open fun deleteObjects(vararg dataObjects: T): Int
    open fun deleteObjects(listObjects: List<T>): Int
}