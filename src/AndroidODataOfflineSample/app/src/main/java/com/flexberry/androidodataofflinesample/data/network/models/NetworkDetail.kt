package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.model.Detail
import java.util.UUID

/**
 * Network representation of [Detail].
 */
@Suppress("kotlin:S117")
data class NetworkDetail(
    val __PrimaryKey : UUID = UUID.randomUUID(),
    val Name: String? = null,
    val Master: NetworkMaster
)