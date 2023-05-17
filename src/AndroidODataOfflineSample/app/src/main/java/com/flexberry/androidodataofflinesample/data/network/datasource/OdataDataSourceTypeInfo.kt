package com.flexberry.androidodataofflinesample.data.network.datasource

class OdataDataSourceTypeInfo(
    val TypeName: String,
    val Namespace: String,
    val OdataTypeName: String,
    val isEnum: Boolean = false
) {
    val fullOdataTypeName = "$Namespace$OdataTypeName"
    val enumFilterTypeName = "$Namespace.$OdataTypeName"
}