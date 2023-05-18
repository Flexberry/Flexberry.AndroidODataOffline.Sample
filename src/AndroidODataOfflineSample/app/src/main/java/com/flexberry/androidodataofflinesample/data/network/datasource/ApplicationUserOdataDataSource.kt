package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser

/**
 * OData источник данных для класса [NetworkApplicationUser]
 */
class ApplicationUserOdataDataSource: OdataDataSource<NetworkApplicationUser>(NetworkApplicationUser::class)
{

}