package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import java.util.Date
import java.util.UUID

/**
 * Network representation of [Vote]
 */
@Suppress("kotlin:S117")
data class NetworkVote(
    val __PrimaryKey : UUID = UUID.randomUUID(),
    val CreateTime: Date? = null,
    val Creator: String? = null,
    val EditTime: Date? = null,
    val Editor: String? = null,
    val VoteType: VoteType? = null,
    val Author: NetworkApplicationUser
)