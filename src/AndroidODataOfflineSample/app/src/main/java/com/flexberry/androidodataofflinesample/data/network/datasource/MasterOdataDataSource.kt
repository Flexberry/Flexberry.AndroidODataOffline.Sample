package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster

/**
 * OData источник данных для класса [NetworkMaster].
 */
class MasterOdataDataSource: OdataDataSource<NetworkMaster>(NetworkMaster::class) {

}