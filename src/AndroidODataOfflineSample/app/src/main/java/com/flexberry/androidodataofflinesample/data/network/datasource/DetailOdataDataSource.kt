package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkDetail
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

/**
 * OData источник данных для класса [NetworkDetail].
 */
class DetailOdataDataSource: OdataDataSource<NetworkDetail>(NetworkDetail::class) {
    /**
     * Получить объекты [NetworkDetail] с детейлами (если будут) и мастерами.
     */
    fun getDetailsFull(querySettings: QuerySettings?): List<NetworkDetail> {
        return this.readObjects(querySettings, NetworkDetail.Views.NetworkDetailE)
    }
}