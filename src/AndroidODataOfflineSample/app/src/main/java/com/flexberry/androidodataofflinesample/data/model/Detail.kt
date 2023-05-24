package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.network.models.NetworkDetail
import java.util.UUID

/**
 * Представление внешнего уровня для [Detail].
 */
data class Detail(
    val primarykey: UUID,
    val name: String? = null,
    val master: Master,
) {
    /**
     * Преобразование [Detail] в [NetworkDetail].
     */
    fun asNetworkModel(): NetworkDetail {
        return NetworkDetail(
            __PrimaryKey = primarykey,
            Name = name,
            Master = master.asNetworkModel()
        )
    }

    /**
     * Преобразование [Detail] в [DetailEntity].
     */
    fun asLocalModel(): DetailEntity {
        return DetailEntity(
            primarykey = primarykey,
            name = name,
            master = master.asLocalModel()
        )
    }
}

/**
 * Преобразование [NetworkDetail] в [Detail].
 */
fun NetworkDetail.asDataModel() = networkDetailAsDataModel(this)

/**
 * Преобразование [DetailEntity] в [Detail].
 */
fun DetailEntity.asDataModel() = detailEntityAsDataModel(this)

/**
 * Преобразование [NetworkDetail] в [Detail].
 */
private fun networkDetailAsDataModel(dataObject: NetworkDetail): Detail {
    return Detail(
        primarykey = dataObject.__PrimaryKey,
        name = dataObject.Name,
        master = dataObject.Master.asDataModel()
    )
}

/**
 * Преобразование [DetailEntity] в [Detail].
 */
private fun detailEntityAsDataModel(entityObject: DetailEntity): Detail {
    return Detail(
        primarykey = entityObject.primarykey,
        name = entityObject.name,
        master = entityObject.master?.asDataModel() ?: Master(entityObject.masterId)
    )
}