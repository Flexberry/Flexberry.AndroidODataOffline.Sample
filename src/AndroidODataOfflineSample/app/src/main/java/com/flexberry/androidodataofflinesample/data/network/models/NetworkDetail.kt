package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.model.Detail
import com.flexberry.androidodataofflinesample.data.query.View
import java.util.UUID

/**
 * Network representation of [Detail].
 */
@Suppress("kotlin:S117")
data class NetworkDetail(
    val __PrimaryKey : UUID = UUID.randomUUID(),
    val Name: String? = null,
    val Master: NetworkMaster
) {
    class Views {
        companion object {
            val NetworkDetailE = View(
                name = "NetworkDetailE",
                stringedView =  """
                        Name,
                        Master.Name
                    """.trimIndent()
            )

            val NetworkDetailD = View(
                name = "NetworkDetailD",
                stringedView =  """
                        Name,
                        Master.Name
                    """.trimIndent()
            )
        }
    }
}