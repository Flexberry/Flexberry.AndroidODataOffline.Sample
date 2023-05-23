package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster
import java.util.UUID

/**
 * Представление внешнего уровня для [Master].
 */
data class Master(
    val primarykey: UUID,
    val name: String? = null,
    val details: List<Detail>? = null
) {
    /**
     * Преобразование [Master] в [NetworkMaster].
     */
    fun asNetworkModel(): NetworkMaster {
        return NetworkMaster(
            __PrimaryKey = primarykey,
            Name = name,
            Detail = details?.map { it.asNetworkModel() }
        )
    }
}

/**
 * Преобразование [NetworkMaster] в [Master].
 */
fun NetworkMaster.asDataModel() = networkMasterAsDataModel(this)

/**
 * Преобразование [NetworkMaster] в [Master].
 */
private fun networkMasterAsDataModel(dataObject: NetworkMaster): Master {
    return Master(
        primarykey = dataObject.__PrimaryKey,
        name = dataObject.Name,
        details = dataObject.Detail?.map { it.asDataModel() }
    )
}