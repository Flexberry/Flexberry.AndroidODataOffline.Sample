package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import kotlin.reflect.KClass

open class OdataDataSource<T : Any>(private val odataObjectClass: KClass<T>)
{
    private val odataDataSourceCommon = OdataDataSourceCommon()

    fun createObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.createObjects(dataObjects.asList())
    }

    fun createObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.createObjects(listObjects)
    }

    fun readObjects(querySettings: QuerySettings? = null): List<T> {
        return odataDataSourceCommon.readObjects(odataObjectClass, querySettings) as List<T>
    }

    fun updateObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.updateObjects(dataObjects.asList())
    }

    fun updateObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.updateObjects(listObjects)
    }

    fun deleteObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.deleteObjects(dataObjects.asList())
    }

    fun deleteObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.deleteObjects(listObjects)
    }
}