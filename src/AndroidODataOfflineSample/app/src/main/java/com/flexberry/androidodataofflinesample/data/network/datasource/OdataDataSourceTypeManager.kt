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
            val ind = odataTypeMap.indexOfFirst { x -> x.first == typeName }

            return if (ind < 0) {
                null
            } else {
                odataTypeMap[ind].second
            }
        }

        fun getTypeName(odataTypeName: String?): String? {
            val ind = odataTypeMap.indexOfFirst { x -> x.second == odataTypeName }

            return if (ind < 0) {
                null
            } else {
                odataTypeMap[ind].first
            }
        }
    }
}