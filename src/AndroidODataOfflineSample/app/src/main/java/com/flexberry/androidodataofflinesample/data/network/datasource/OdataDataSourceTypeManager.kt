package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote

class OdataDataSourceTypeManager {
    companion object {
        private val odataTypeMap: List<Pair<String, String>> = listOf(
            Pair(NetworkApplicationUser::class.simpleName!!, "EmberFlexberryDummyApplicationUsers"),
            Pair(NetworkVote::class.simpleName!!, "EmberFlexberryDummyVotes")
        )

        fun getOdataTypeName(typeName: String?): String? {
            return odataTypeMap.firstOrNull { x -> x.first == typeName }?.second
        }

        fun getTypeName(odataTypeName: String?): String? {
            return odataTypeMap.firstOrNull { x -> x.second == odataTypeName }?.first
        }
    }
}