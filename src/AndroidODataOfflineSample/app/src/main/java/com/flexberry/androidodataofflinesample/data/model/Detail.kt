package com.flexberry.androidodataofflinesample.data.model

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
}

/**
 * Преобразование [NetworkDetail] в [Detail].
 */
fun NetworkDetail.asDataModel() = networkDetailAsDataModel(this)

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