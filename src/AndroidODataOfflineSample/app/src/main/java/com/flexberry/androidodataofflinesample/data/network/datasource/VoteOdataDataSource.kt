package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.datasource.odata.OdataDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote

/**
 * OData источник данных для класса [NetworkVote]
 */
class VoteOdataDataSource: OdataDataSource<NetworkVote>(NetworkVote::class)
{

}