package com.flexberry.androidodataofflinesample.data.network.datasource.odata

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote

/**
 * Менеджер типов OData.
 */
class OdataDataSourceTypeManager {
    companion object {
        /**
         * Список типов [OdataDataSourceTypeInfo].
         */
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

        /**
         * Получить информацию о типе по его имени.
         *
         * @param typeName Имя типа.
         * @return [OdataDataSourceTypeInfo] указанного типа.
         */
        fun getInfoByTypeName(typeName: String?): OdataDataSourceTypeInfo<*>? {
            return odataTypeMap.firstOrNull { x -> x.typeName == typeName }
        }

        /**
         * Получить информацию о типе по его имени в OData.
         *
         * @param odataTypeName Имя типа в OData.
         * @return [OdataDataSourceTypeInfo] указанного типа.
         */
        fun getInfoByOdataTypeName(odataTypeName: String?): OdataDataSourceTypeInfo<*>? {
            return odataTypeMap.firstOrNull { x -> x.odataTypeName == odataTypeName
                    || x.fullOdataTypeName == odataTypeName }
        }
    }
}