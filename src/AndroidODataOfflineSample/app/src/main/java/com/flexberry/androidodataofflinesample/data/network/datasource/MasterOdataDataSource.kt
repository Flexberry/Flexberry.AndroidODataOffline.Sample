package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

/**
 * OData источник данных для класса [NetworkMaster].
 */
class MasterOdataDataSource: OdataDataSource<NetworkMaster>(NetworkMaster::class) {
    /**
     * Получить объекты [NetworkMaster] с детейлами и мастерами (если будут).
     */
    fun getMastersFull(querySettings: QuerySettings?): List<NetworkMaster> {
        return this.readObjects(querySettings, NetworkMaster.Views.NetworkMasterE)
    }
}