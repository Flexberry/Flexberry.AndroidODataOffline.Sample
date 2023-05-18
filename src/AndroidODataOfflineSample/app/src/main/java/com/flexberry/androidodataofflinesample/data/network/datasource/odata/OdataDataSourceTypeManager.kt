package com.flexberry.androidodataofflinesample.data.network.datasource.odata

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import java.util.Dictionary

class OdataDataSourceTypeManager {
    companion object {
        private val odataTypeMap  = listOf(
            OdataDataSourceTypeInfo(
                kotlinClass = NetworkApplicationUser::class,
                namespace = "EmberFlexberryDummy",
                odataTypeName = "ApplicationUsers",
                details = listOf("Votes")),
            OdataDataSourceTypeInfo(
                kotlinClass = NetworkVote::class,
                namespace = "EmberFlexberryDummy",
                odataTypeName = "Votes"
            ),
            OdataDataSourceTypeInfo(
                kotlinClass = VoteType::class,
                namespace = "EmberFlexberryDummy",
                odataTypeName = "VoteType",
                isEnum = true)
        )

        private val odataDataSources: Dictionary<String, Any>? = null

        fun getInfoByTypeName(typeName: String?): OdataDataSourceTypeInfo<*>? {
            return odataTypeMap.firstOrNull { x -> x.typeName == typeName }
        }

        fun getInfoByOdataTypeName(odataTypeName: String?): OdataDataSourceTypeInfo<*>? {
            return odataTypeMap.firstOrNull { x -> x.odataTypeName == odataTypeName
                    || x.fullOdataTypeName == odataTypeName }
        }
    }
}