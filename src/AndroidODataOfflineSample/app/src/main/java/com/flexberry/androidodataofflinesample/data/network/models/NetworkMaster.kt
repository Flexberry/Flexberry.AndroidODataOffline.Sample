package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.data.query.View
import java.util.UUID

/**
 * Network representation of [Master].
 */
@Suppress("kotlin:S117")
data class NetworkMaster(
    val __PrimaryKey : UUID = UUID.randomUUID(),
    val Name: String? = null,
    var Detail: List<NetworkDetail>? = null
) {
    class Views {
        companion object {
            val NetworkMasterE = View(
                name = "NetworkMasterE",
                stringedView =  """
                        ${NetworkMaster::Name.name}
                    """.trimIndent()
            ).addDetail(NetworkMaster::Detail.name, NetworkDetail.Views.NetworkDetailD)

            val NetworkMasterL = View(
                name = "NetworkMasterL",
                stringedView =  """
                        ${NetworkMaster::Name.name}
                    """.trimIndent()
            )
        }
    }
}
