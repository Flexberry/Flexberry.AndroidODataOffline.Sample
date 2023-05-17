package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote

class OdataDataSourceTypeManager {
    companion object {
        private val odataTypeMap: List<OdataDataSourceTypeInfo> = listOf(
            OdataDataSourceTypeInfo(
                NetworkApplicationUser::class.simpleName!!,
                "EmberFlexberryDummy",
                "ApplicationUsers"),
            OdataDataSourceTypeInfo(
                NetworkVote::class.simpleName!!,
                "EmberFlexberryDummy",
                "Votes"),
            OdataDataSourceTypeInfo(
                VoteType::class.simpleName!!,
                "EmberFlexberryDummy",
                "VoteType",
                true)
        )

        fun getInfoByTypeName(typeName: String?): OdataDataSourceTypeInfo? {
            return odataTypeMap.firstOrNull { x -> x.TypeName == typeName }
        }

        fun getInfoByOdataTypeName(odataTypeName: String?): OdataDataSourceTypeInfo? {
            return odataTypeMap.firstOrNull { x -> x.OdataTypeName == odataTypeName
                    || x.fullOdataTypeName == odataTypeName }
        }
    }
}