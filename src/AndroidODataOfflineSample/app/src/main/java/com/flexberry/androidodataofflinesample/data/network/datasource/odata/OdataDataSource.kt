package com.flexberry.androidodataofflinesample.data.network.datasource.odata

import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import kotlin.reflect.KClass

open class OdataDataSource<T : Any> (private val odataObjectClass: KClass<T>) : NetworkDataSource<T>
{
    private val odataDataSourceCommon = OdataDataSourceCommon()

    override fun createObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.createObjects(dataObjects.asList())
    }

    override fun createObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.createObjects(listObjects)
    }

    override fun readObjects(querySettings: QuerySettings?): List<T> {
        return odataDataSourceCommon.readObjects(odataObjectClass, querySettings) as List<T>
    }

    override fun updateObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.updateObjects(dataObjects.asList())
    }

    override fun updateObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.updateObjects(listObjects)
    }

    override fun deleteObjects(vararg dataObjects: T): Int {
        return odataDataSourceCommon.deleteObjects(dataObjects.asList())
    }

    override fun deleteObjects(listObjects: List<T>): Int {
        return odataDataSourceCommon.deleteObjects(listObjects)
    }
}