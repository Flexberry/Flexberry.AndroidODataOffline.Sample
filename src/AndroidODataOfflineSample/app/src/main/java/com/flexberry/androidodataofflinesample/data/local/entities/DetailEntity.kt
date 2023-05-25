package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.flexberry.androidodataofflinesample.data.query.View
import java.util.UUID

/**
 * Сущность [Master].
 */
@Entity(tableName = DetailEntity.tableName)
data class DetailEntity(
    @PrimaryKey
    @ColumnInfo
    val primarykey: UUID = UUID.randomUUID(),

    @ColumnInfo
    val name: String? = null,

    @ColumnInfo
    var masterId: UUID,
) {
    constructor(
        primarykey: UUID = UUID.randomUUID(),
        name: String? = null,
        master: MasterEntity
    ) : this(
        primarykey = primarykey,
        name = name,
        masterId = master.primarykey) {
        this.master = master
    }

    @Ignore
    var master: MasterEntity? = null

    class Views {
        companion object {
            val DetailEntityE = View(
                name = "DetailEntityE",
                stringedView = """
                    name,
                    masterId,
                    master.name
                """.trimIndent()
            )

            val DetailEntityD = View(
                name = "DetailEntityD",
                stringedView = """
                    name,
                    masterId
                """.trimIndent()
            )
        }
    }

    companion object {
        const val tableName = "Detail"
    }
}