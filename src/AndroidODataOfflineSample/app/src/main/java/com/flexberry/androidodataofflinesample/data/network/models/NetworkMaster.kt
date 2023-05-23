package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.model.Master
import java.util.UUID

/**
 * Network representation of [Master].
 */
@Suppress("kotlin:S117")
data class NetworkMaster(
    val __PrimaryKey : UUID = UUID.randomUUID(),
    val Name: String? = null,
    var Detail: List<NetworkDetail>? = null
)
