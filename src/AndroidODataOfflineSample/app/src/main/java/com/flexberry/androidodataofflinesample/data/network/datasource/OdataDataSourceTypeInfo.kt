package com.flexberry.androidodataofflinesample.data.network.datasource

class OdataDataSourceTypeInfo(
    val typeName: String,
    val namespace: String,
    val odataTypeName: String,
    val isEnum: Boolean = false
) {
    val fullOdataTypeName = "$namespace$odataTypeName"
    val enumFilterTypeName = "$namespace.$odataTypeName"
}