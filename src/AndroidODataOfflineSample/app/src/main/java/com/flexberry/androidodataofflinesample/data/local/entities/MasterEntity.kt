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
@Entity(tableName = MasterEntity.tableName)
data class MasterEntity(
    @PrimaryKey
    @ColumnInfo
    val primarykey: UUID = UUID.randomUUID(),

    @ColumnInfo
    val name: String? = null,
) {
    constructor(
        primarykey: UUID = UUID.randomUUID(),
        name: String? = null,
        details: List<DetailEntity>? = null
    ) : this(
        primarykey = primarykey,
        name = name
    ) {
        this.details = details
    }

    @Ignore
    var details: List<DetailEntity>? = null

    class Views {
        companion object {
            val MasterEntityE = View(
                name = "MasterEntityE",
                stringedView = """
                    ${MasterEntity::name.name}
                """.trimIndent()
            ).addDetail(MasterEntity::details.name, DetailEntity.Views.DetailEntityD)

            val MasterEntityL = View(
                name = "MasterEntityL",
                stringedView = """
                    ${MasterEntity::name.name}
                """.trimIndent()
            )
        }
    }

    companion object {
        const val tableName = "Master"
    }
}