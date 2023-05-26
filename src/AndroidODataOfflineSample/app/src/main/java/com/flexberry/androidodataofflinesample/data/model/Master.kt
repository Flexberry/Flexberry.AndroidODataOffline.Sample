package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster
import java.util.UUID

/**
 * Представление внешнего уровня для [Master].
 */
data class Master(
    val primarykey: UUID,
    var name: String? = null,
    var details: List<Detail>? = null
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

    /**
     * Преобразование [Master] в [MasterEntity].
     */
    fun asLocalModel(): MasterEntity {
        return MasterEntity(
            primarykey = this.primarykey,
            name = this.name,
            details = this.details?.map { it.asLocalModel() }
        )
    }
}

/**
 * Преобразование [NetworkMaster] в [Master].
 */
fun NetworkMaster.asDataModel() = networkMasterAsDataModel(this)

/**
 * Преобразование [MasterEntity] в [Master].
 */
fun MasterEntity.asDataModel() = masterEntityAsDataModel(this)

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

/**
 * Преобразование [MasterEntity] в [Master].
 */
private fun masterEntityAsDataModel(entityObject: MasterEntity): Master {
    return Master(
        primarykey = entityObject.primarykey,
        name = entityObject.name,
        details = entityObject.details?.map { it.asDataModel() }
    )
}