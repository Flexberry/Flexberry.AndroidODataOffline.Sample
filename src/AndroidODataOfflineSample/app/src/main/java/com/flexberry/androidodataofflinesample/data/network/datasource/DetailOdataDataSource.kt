package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkDetail

/**
 * OData источник данных для класса [NetworkDetail].
 */
class DetailOdataDataSource: OdataDataSource<NetworkDetail>(NetworkDetail::class) {

}