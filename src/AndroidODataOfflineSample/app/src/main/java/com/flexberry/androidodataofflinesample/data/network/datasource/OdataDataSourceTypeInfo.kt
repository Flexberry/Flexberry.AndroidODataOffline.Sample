package com.flexberry.androidodataofflinesample.data.network.datasource

import kotlin.reflect.KClass

class OdataDataSourceTypeInfo<T : Any>(
    val kotlinClass: KClass<T>,
    val namespace: String,
    val odataTypeName: String,
    val isEnum: Boolean = false,
    val details: List<String>? = null
) {
    val typeName = kotlinClass.simpleName!!
    val fullOdataTypeName = "$namespace$odataTypeName"
    val enumFilterTypeName = "$namespace.$odataTypeName"

    fun hasDetail(detailName: String?): Boolean {
        return details?.contains(detailName) ?: false
    }
}