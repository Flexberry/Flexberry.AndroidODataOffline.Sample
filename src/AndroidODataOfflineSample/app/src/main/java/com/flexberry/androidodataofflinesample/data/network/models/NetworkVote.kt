package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.query.View
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
) {
    class Views {
        companion object {
            val NetworkVoteD = View(
                name = "NetworkVoteD",
                stringedView = """
                        __PrimaryKey,
                        CreateTime,
                        Creator,
                        EditTime,
                        Editor,
                        VoteType,
                        Author.__PrimaryKey,
                        Author.Name
                    """.trimIndent()
            )

            val NetworkVoteL = View(
                name = "NetworkVoteL",
                stringedView = """
                        __PrimaryKey,
                        CreateTime,
                        Creator,
                        EditTime,
                        Editor,
                        VoteType,
                        Author.Name
                    """.trimIndent()
            )
        }
    }
}